package org.example.project.details

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.project.core.ControlledFakeRunAsync
import org.example.project.core.domain.FakeManageResource
import org.example.project.details.domain.DetailsRepository
import org.example.project.details.domain.ReadmeNotFoundException
import org.example.project.details.domain.RepoDetails
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
    private lateinit var detailsRepository: FakeDetailsRepository
    private lateinit var runAsync: ControlledFakeRunAsync
    private lateinit var detailsUiMapper: RepoDetails.Mapper<DetailsUiState>
    private lateinit var manageResource: FakeManageResource

    @BeforeTest
    fun setUp() {
        manageResource = FakeManageResource()
        detailsUiMapper = DetailsUiMapper()
        detailsRepository = FakeDetailsRepository()
        runAsync = ControlledFakeRunAsync()

    }

    @Test //code will be in init block
    fun `success load details and readme`() {
        detailsRepository.isRepoDetailsFailure(false)
        detailsRepository.isReadmeFailure(false)

        viewModel = DetailsViewModel(
            detailsUiMapper = DetailsUiMapper(),
            detailsRepository = detailsRepository,
            runAsync = runAsync,
            manageResource = manageResource,
            savedStateHandle = SavedStateHandle()
        )
        val detailsScreenState: StateFlow<DetailsScreenState> = viewModel.detailsScreenState

        assertEquals(initialScreenState, detailsScreenState.value)

        runAsync.invokeUi()

        assertEquals(
            successDetailsScreenState,
            detailsScreenState.value
        )
    }

    @Test
    fun `success load details but readme does not exist`() {
        detailsRepository.isRepoDetailsFailure(false)
        detailsRepository.isReadmeFailure(true)

        viewModel = DetailsViewModel(
            detailsUiMapper = DetailsUiMapper(),
            detailsRepository = detailsRepository,
            runAsync = runAsync,
            manageResource = manageResource,
            savedStateHandle = SavedStateHandle()
        )

        runAsync.invokeUi()
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
        detailsRepository.isRepoDetailsFailure(true)
        detailsRepository.isReadmeFailure(false)

        viewModel = DetailsViewModel(
            detailsUiMapper = DetailsUiMapper(),
            detailsRepository = detailsRepository,
            runAsync = runAsync,
            manageResource = manageResource,
            savedStateHandle = SavedStateHandle()
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


        detailsRepository.isRepoDetailsFailure(false)

        viewModel.retry()


        assertEquals(
            DetailsScreenState(
                isRefreshing = false,
                detailsUiState = DetailsUiState.Loading,
            ),
            uiState.value,
        )

        runAsync.invokeUi()


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
        detailsRepository.isRepoDetailsFailure(false)
        detailsRepository.isReadmeFailure(false)

        viewModel = DetailsViewModel(
            detailsUiMapper = DetailsUiMapper(),
            detailsRepository = detailsRepository,
            runAsync = runAsync,
            manageResource = manageResource,
            savedStateHandle = SavedStateHandle()
        )
        runAsync.invokeUi()

        val uiState: StateFlow<DetailsScreenState> = viewModel.detailsScreenState
        assertEquals(successDetailsScreenState.copy(isRefreshing = false), uiState.value)

        viewModel.refresh()

        detailsRepository.checkRefreshDetailsIsCalled(1)
        detailsRepository.checkRefreshReadmeIsCalled(1)

        assertEquals(successDetailsScreenState.copy(isRefreshing = true), uiState.value)

        runAsync.invokeUi()

        assertEquals(successRefreshScreenState, uiState.value)
    }

    @Test
    fun `navigate to create issues page`() = runBlocking {
        val emitedEvents = mutableListOf<DetailsEvent>()
        viewModel = DetailsViewModel(
            detailsUiMapper = DetailsUiMapper(),
            detailsRepository = detailsRepository,
            runAsync = runAsync,
            manageResource = manageResource,
            savedStateHandle = SavedStateHandle()
        )
        val detailsEventFlow: SharedFlow<DetailsEvent> = viewModel.detailsEvent
        val job: Job = launch(Dispatchers.Unconfined) {

            detailsEventFlow.collect {
                println(it.toString() + "EMITED")
                emitedEvents.add(it)
            }
        }

        viewModel.onCreateIssues(repoOwner = "scope", repoName = "repo name")

        runAsync.invokeUi()

        job.cancel()

        assertEquals(1, emitedEvents.size)
        assertEquals(
            emitedEvents[0],
            DetailsEvent.OnCreateIssues(repoOwner = "scope", repoName = "repo name")
        )
        Unit
    }

    @Test
    fun `navigate to code page`() = runBlocking {
        val emitedEvents = mutableListOf<DetailsEvent>()
        viewModel = DetailsViewModel(
            detailsUiMapper = DetailsUiMapper(),
            detailsRepository = detailsRepository,
            runAsync = runAsync,
            manageResource = manageResource,
            savedStateHandle = SavedStateHandle()
        )
        val detailEventFlow: SharedFlow<DetailsEvent> = viewModel.detailsEvent
        val job: Job? = launch {
            detailEventFlow.collect {
                emitedEvents.add(it)
            }
        }

        viewModel.onCode(repoOwner = "scope", repoName = "repo name")

        runAsync.invokeUi()

        assertEquals(emitedEvents.size, 1)
        assertEquals(
            emitedEvents[0],
            DetailsEvent.OnCode(repoOwner = "scope", repoName = "repo name")
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

private class FakeDetailsRepository : DetailsRepository {

    private var isDetailsFailure = false
    private var isReadmeFailure = false

    private var refreshReadme = 0
    private var refreshDetails = 0
    override suspend fun repoDetails(repoOwner: String, repoName: String): Result<RepoDetails> {
        if (isDetailsFailure) {
            return Result.failure(IllegalStateException(""))
        } else {
            return Result.success(
                RepoDetails(
                    repoOwner = "scope",
                    repoName = "repo name",
                    repoDesc = "repo dsec",
                    forksCount = 1,
                    issuesCount = 1,
                    programmingLanguage = "kotlin",
                )
            )
        }
    }

    override suspend fun readme(repoOwner: String, repoName: String): Result<String> {

        if (this.isReadmeFailure) {
            return Result.failure<String>(ReadmeNotFoundException)
        } else {
            return Result.success("success readme")
        }
    }

    override suspend fun refreshReadme(repoOwner: String, repoName: String): Result<String> {
        refreshReadme++
        return Result.success("success refresh readme")
    }

    override suspend fun refreshDetails(repoOwner: String, repoName: String): Result<RepoDetails> {
        refreshDetails++
        return Result.success(
            RepoDetails(
                repoOwner = "scope",
                repoName = "repo name",
                repoDesc = "repo dsec",
                forksCount = 150,
                issuesCount = 1,
                programmingLanguage = "kotlin",
            )
        )
    }

    fun isRepoDetailsFailure(flag: Boolean) {
        isDetailsFailure = flag
    }

    fun isReadmeFailure(flag: Boolean) {
        this.isReadmeFailure = flag
    }

    fun checkRefreshDetailsIsCalled(expectedTimes: Int) {
        assertEquals(expectedTimes, refreshDetails)
    }

    fun checkRefreshReadmeIsCalled(expectedTimes: Int) {
        assertEquals(expectedTimes, refreshReadme)
    }
}
