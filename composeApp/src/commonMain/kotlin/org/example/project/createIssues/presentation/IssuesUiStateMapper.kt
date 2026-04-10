package org.example.project.createIssues.presentation

import org.example.project.createIssues.domain.IssueResult

class IssuesUiStateMapper : IssueResult.Mapper<IssueScreenState> {

    override fun mapLongTitle(
        message: String,
        current: IssueScreenState
    ): IssueScreenState {
        return current.copy(
            isCreateButtonActive = true,
            buttonState = ButtonState.Init,
            titleSuppText = message
        )
    }

    override fun mapLongBody(
        message: String,
        current: IssueScreenState
    ): IssueScreenState {
        return current.copy(
            isCreateButtonActive = true,
            buttonState = ButtonState.Init,
            bodySuppText = message
        )
    }

    override fun mapFailure(
        message: String,
        current: IssueScreenState
    ): IssueScreenState {
        return current.copy(
            isCreateButtonActive = true,
            buttonState = ButtonState.Failure(message),
        )
    }

    override fun mapSuccess(current: IssueScreenState): IssueScreenState {
        return current.copy(
            isSuccess = true,
        )
    }

    override fun mapAllLong(
        titleMessage: String,
        bodyMessage: String,
        current: IssueScreenState
    ): IssueScreenState {
        return current.copy(
            bodySuppText = bodyMessage,
            titleSuppText = titleMessage,
            isCreateButtonActive = true,
            buttonState = ButtonState.Init,
        )
    }
}
