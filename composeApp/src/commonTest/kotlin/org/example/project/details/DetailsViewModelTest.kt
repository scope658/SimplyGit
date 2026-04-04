package org.example.project.details

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow
import org.example.project.core.ControlledFakeRunAsync
import org.example.project.core.domain.FakeManageResource
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DetailsViewModelTest {

    private lateinit var viewModel: DetailsViewModel
    private lateinit var detailsRepository: FakeDetailsRepository
    private lateinit var runAsync: ControlledFakeRunAsync
    private lateinit var detailsUiMapper: RepoDetails.Mapper<DetailsUIState>
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
            manageResourse = manageResource,
            savedStateHandle = SavedStateHandle()
        )
        val detailsScreenState: StateFlow<DetailsScreenState> = viewModel.detailsScreenState

        assertEquals(initialScreenState, detailsScreenState.value)

        runAsync.invokeUi()

        assertEquals(
            successDetailsScreenState.copy(readmeUiState = successReadmeUiState),
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
            manageResourse = manageResource,
            savedStateHandle = SavedStateHandle()
        )

        runAsync.invokeUi()
        assertEquals(successDetailsScreenState)
    }

    @Test
    fun `failure load details then success`() {
        detailsRepository.isRepoDetailsFailure(true)
        detailsRepository.isReadmeFailure(false)

        viewModel = DetailsViewModel(
            detailsUiMapper = DetailsUiMapper(),
            detailsRepository = detailsRepository,
            runAsync = runAsync,
            manageResourse = manageResource,
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


        detailsRepository.isReadmeFailure(false)

        viewModel.retry()


        assertEquals(
            DetailsScreenState(
                isRefreshing = false,
                detailsUiState = DetailsUiState.Loading,
            ),
            uiState.value,
        )

        runAsync.invokeUi()


        assertEquals(successDetailsScreenState, uiState.value)

    }

    @Test
    fun `refresh details and readme`() {
        detailsRepository.isRepoDetailsFailure(false)
        detailsRepository.isReadmeFailure(false)

        viewModel = DetailsViewModel(
            detailsUiMapper = DetailsUiMapper(),
            detailsRepository = detailsRepository,
            runAsync = runAsync,
            manageResourse = manageResource,
            savedStateHandle = SavedStateHandle()
        )
        runAsync.invokeUi()

        val uiState: StateFlow<DetailsScreenState> = viewModel.detailsUiState
        assertEquals(successDetailsScreenState.copy(isRefreshing = false), uiState.value)

        viewModel.refresh()

        detailsRepository.checkRefreshDetailsIsCalled(1)
        detailsRepository.checkRefreshReadmeIsCalled(1)

        assertEquals(successDetailsScreenState.copy(isRefreshing = true), uiState.value)

        runAsync.invokeUi()

        assertEquals(successRefreshScreenState, uiState.value)
    }

}

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
private val successDetailsUiState = DetailsUiState.Success(
    repoName = "repo name",
    repoDesc = "repo dsec",
    forksCount = "1",
    issuescount = "1",
    programmingLanguage = "kotlin",
    readme = ReadmeUiState.Success("readme not found")
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
    override suspend fun repoDetails(): RepoDetails {
        if (isDetailsFailure) {
            throw IllegalStateException("")
        } else {
            return RepoDetails(
                repoName = "repo name",
                repoDesc = "repo dsec",
                forksCount = 1,
                issuescount = 1,
                programmingLanguage = "kotlin",
            )
        }
    }

    override suspend fun readme(): String {

        if (this.isReadmeFailure) {
            throw ReadmeNotFoundException
        } else {
            return "success readme"
        }
    }

    override suspend fun refreshReadme(): String {
        refreshReadme++
        return "success refresh readme"
    }

    override suspend fun refreshDetails(): RepoDetails {
        refreshDetails++
        return RepoDetails(
            repoName = "repo name",
            repoDesc = "repo dsec",
            forksCount = 150,
            issuescount = 1,
            programmingLanguage = "kotlin",
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
