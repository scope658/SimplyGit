package org.example.project.details

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.project.core.ControlledFakeRunAsync
import org.example.project.core.domain.FakeManageResource
import org.example.project.core.presentation.RouteArgs
import org.example.project.details.domain.CombinedDetailsResult
import org.example.project.details.domain.RepoDetails
import org.example.project.details.domain.RepoDetailsUseCase
import org.example.project.details.presentation.DetailsEvent
import org.example.project.details.presentation.DetailsScreenState
import org.example.project.details.presentation.DetailsUiMapper
import org.example.project.details.presentation.DetailsUiState
import org.example.project.details.presentation.DetailsViewModel
import org.example.project.details.presentation.ReadmeUiState
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DetailsViewModelTest {

    private lateinit var viewModel: DetailsViewModel
    private lateinit var fakeRepoDetailsUseCase: FakeRepoDetailsUseCase
    private lateinit var runAsync: ControlledFakeRunAsync
    private lateinit var detailsUiMapper: CombinedDetailsResult.Mapper<DetailsUiState>
    private lateinit var manageResource: FakeManageResource
    private lateinit var detailsArgs: RouteArgs

    @BeforeTest
    fun setUp() {
        detailsArgs = FakeDetailsArgs()
        manageResource = FakeManageResource()
        detailsUiMapper = DetailsUiMapper()
        fakeRepoDetailsUseCase = FakeRepoDetailsUseCase()
        runAsync = ControlledFakeRunAsync()
    }

    @Test //code will be in init block
    fun `success load details and readme`() {
        fakeRepoDetailsUseCase.isRepoDetailsFailure(false)
        fakeRepoDetailsUseCase.isReadmeFailure(false)

        viewModel = DetailsViewModel(
            detailsUiMapper = DetailsUiMapper(),
            repoDetailsUseCase = fakeRepoDetailsUseCase,
            runAsync = runAsync,
            detailsArgs = detailsArgs,
        )
        val detailsScreenState: StateFlow<DetailsScreenState> = viewModel.detailsScreenState

        assertEquals(initialScreenState, detailsScreenState.value)

        runAsync.invokeUi()

        fakeRepoDetailsUseCase.checkRepoDetailsIsCalled("fake repo owner", "fake repo name")
        assertEquals(
            successDetailsScreenState,
            detailsScreenState.value
        )
    }

    @Test
    fun `success load details but readme does not exist`() {
        fakeRepoDetailsUseCase.isRepoDetailsFailure(false)
        fakeRepoDetailsUseCase.isReadmeFailure(true)

        viewModel = DetailsViewModel(
            detailsUiMapper = DetailsUiMapper(),
            repoDetailsUseCase = fakeRepoDetailsUseCase,
            runAsync = runAsync,
            detailsArgs = detailsArgs,
        )

        runAsync.invokeUi()

        fakeRepoDetailsUseCase.checkRepoDetailsIsCalled("fake repo owner", "fake repo name")
        assertEquals(
            successDetailsScreenState.copy(
                detailsUiState = successDetailsUiState.copy(
                    readme = ReadmeUiState.Success("readme not found")
                )
            ), viewModel.detailsScreenState.value
        )
    }

    @Test
    fun `failure load details then success`() {
        fakeRepoDetailsUseCase.isRepoDetailsFailure(true)
        fakeRepoDetailsUseCase.isReadmeFailure(false)

        viewModel = DetailsViewModel(
            detailsUiMapper = DetailsUiMapper(),
            repoDetailsUseCase = fakeRepoDetailsUseCase,
            runAsync = runAsync,
            detailsArgs = detailsArgs,
        )

        val uiState: StateFlow<DetailsScreenState> = viewModel.detailsScreenState

        runAsync.invokeUi()
        assertEquals(
            DetailsScreenState(
                isRefreshing = false,
                detailsUiState = failureDetailsUiState,
            ),
            uiState.value,
        )


        fakeRepoDetailsUseCase.isRepoDetailsFailure(false)

        viewModel.retry()


        assertEquals(
            DetailsScreenState(
                isRefreshing = false,
                detailsUiState = DetailsUiState.Loading,
            ),
            uiState.value,
        )

        runAsync.invokeUi()

        fakeRepoDetailsUseCase.checkRepoDetailsIsCalled("fake repo owner", "fake repo name")
        assertEquals(
            successDetailsScreenState.copy(
                detailsUiState = successDetailsUiState.copy(
                    readme = successReadmeUiState
                )
            ), uiState.value
        )

    }

    @Test
    fun `refresh details and readme`() {
        fakeRepoDetailsUseCase.isRepoDetailsFailure(false)
        fakeRepoDetailsUseCase.isReadmeFailure(false)

        viewModel = DetailsViewModel(
            detailsUiMapper = DetailsUiMapper(),
            repoDetailsUseCase = fakeRepoDetailsUseCase,
            runAsync = runAsync,
            detailsArgs = detailsArgs,
        )
        runAsync.invokeUi()

        val uiState: StateFlow<DetailsScreenState> = viewModel.detailsScreenState
        assertEquals(successDetailsScreenState.copy(isRefreshing = false), uiState.value)

        viewModel.refresh()

        fakeRepoDetailsUseCase.checkRefreshIsCalled(1, "fake repo owner", "fake repo name")

        assertEquals(successDetailsScreenState.copy(isRefreshing = true), uiState.value)

        runAsync.invokeUi()

        assertEquals(successRefreshScreenState, uiState.value)
    }

    @Test
    fun `navigate to create issues page`() = runBlocking {
        val emitedEvents = mutableListOf<DetailsEvent>()
        viewModel = DetailsViewModel(
            detailsUiMapper = DetailsUiMapper(),
            repoDetailsUseCase = fakeRepoDetailsUseCase,
            runAsync = runAsync,
            detailsArgs = detailsArgs,
        )
        val detailsEventFlow: SharedFlow<DetailsEvent> = viewModel.detailsEvent
        val job: Job = launch {

            detailsEventFlow.collect {
                println(it.toString() + "EMITED")
                emitedEvents.add(it)
            }
        }

        viewModel.onCreateIssues()

        runAsync.invokeUi()

        job.cancel()

        assertEquals(1, emitedEvents.size)
        assertEquals(
            emitedEvents[0],
            DetailsEvent.OnCreateIssues(repoOwner = "fake repo owner", repoName = "fake repo name")
        )
        Unit
    }

    @Test
    fun `navigate to code page`() = runBlocking {
        val emitedEvents = mutableListOf<DetailsEvent>()
        viewModel = DetailsViewModel(
            detailsUiMapper = DetailsUiMapper(),
            repoDetailsUseCase = fakeRepoDetailsUseCase,
            runAsync = runAsync,
            detailsArgs = detailsArgs,
        )
        val detailEventFlow: SharedFlow<DetailsEvent> = viewModel.detailsEvent
        val job: Job? = launch {
            detailEventFlow.collect {
                emitedEvents.add(it)
            }
        }

        viewModel.onCode()

        runAsync.invokeUi()

        assertEquals(emitedEvents.size, 1)
        assertEquals(
            emitedEvents[0],
            DetailsEvent.OnCode(repoOwner = "fake repo owner", repoName = "fake repo name")
        )

        job?.cancel()
        Unit
    }
}

private val successDetailsUiState = DetailsUiState.Success(
    repoOwner = "scope",
    repoName = "repo name",
    repoDesc = "repo dsec",
    forksCount = "1",
    issuesCount = "1",
    programmingLanguage = "kotlin",
    readme = ReadmeUiState.Success("success readme")
)

private val successRefreshScreenState = DetailsScreenState(
    isRefreshing = false,
    detailsUiState = successDetailsUiState.copy(
        forksCount = "150",
        readme = ReadmeUiState.Success("success refresh readme")
    )
)

private val initialScreenState = DetailsScreenState(
    isRefreshing = false,
    detailsUiState = DetailsUiState.Loading,
)


private val successDetailsScreenState = DetailsScreenState(
    isRefreshing = false,
    detailsUiState = successDetailsUiState,
)

private val failureDetailsUiState = DetailsUiState.Failure(
    message = "service unavailable"
)
private val successReadmeUiState = ReadmeUiState.Success(
    readme = "success readme"
)

private class FakeRepoDetailsUseCase : RepoDetailsUseCase {

    private var isDetailsFailure = false
    private var isReadmeFailure = false

    private var refreshCalledTimes = 0

    private lateinit var repoOwnerArg: String
    private lateinit var repoNameArg: String

    fun isRepoDetailsFailure(flag: Boolean) {
        isDetailsFailure = flag
    }

    fun isReadmeFailure(flag: Boolean) {
        this.isReadmeFailure = flag
    }

    fun checkRefreshIsCalled(
        expectedTimes: Int,
        expectedRepoOwner: String,
        expectedRepoName: String
    ) {
        assertEquals(expectedRepoOwner, repoOwnerArg)
        assertEquals(expectedRepoName, repoNameArg)
        assertEquals(expectedTimes, refreshCalledTimes)

    }

    fun checkRepoDetailsIsCalled(expectedRepoOwner: String, expectedRepoName: String) {
        assertEquals(expectedRepoOwner, repoOwnerArg)
        assertEquals(expectedRepoName, repoNameArg)
    }

    override suspend fun repoDetails(repoOwner: String, repoName: String): CombinedDetailsResult {
        repoOwnerArg = repoOwner
        repoNameArg = repoName

        if (isDetailsFailure) {
            return CombinedDetailsResult.Failure("service unavailable")
        } else {
            return CombinedDetailsResult.Success(
                RepoDetails(
                    repoOwner = "scope",
                    repoName = "repo name",
                    repoDesc = "repo dsec",
                    forksCount = 1,
                    issuesCount = 1,
                    programmingLanguage = "kotlin",
                ),
                readme = if (isReadmeFailure) "readme not found" else "success readme",
            )
        }
    }

    override suspend fun refreshRepoDetails(
        repoOwner: String,
        repoName: String
    ): CombinedDetailsResult {
        refreshCalledTimes++
        repoOwnerArg = repoOwner
        repoNameArg = repoName

        if (isDetailsFailure) {
            return CombinedDetailsResult.Failure("service unavailable")
        } else {
            return CombinedDetailsResult.Success(
                RepoDetails(
                    repoOwner = "scope",
                    repoName = "repo name",
                    repoDesc = "repo dsec",
                    forksCount = 150,
                    issuesCount = 1,
                    programmingLanguage = "kotlin",
                ),
                readme = if (isReadmeFailure) "readme not found" else "success refresh readme",
            )
        }
    }
}

private class FakeDetailsArgs : RouteArgs {
    override fun repoOwner(): String {
        return "fake repo owner"
    }

    override fun repoName(): String {
        return "fake repo name"
    }
}
