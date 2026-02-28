package org.example.project.onboarding.presentation

import ktshwnumbertwo.composeapp.generated.resources.Res
import ktshwnumbertwo.composeapp.generated.resources.first_onboarding_image
import ktshwnumbertwo.composeapp.generated.resources.onboarding_first_desc
import ktshwnumbertwo.composeapp.generated.resources.onboarding_first_title
import ktshwnumbertwo.composeapp.generated.resources.onboarding_second_desc
import ktshwnumbertwo.composeapp.generated.resources.onboarding_second_title
import ktshwnumbertwo.composeapp.generated.resources.onboarding_third_desc
import ktshwnumbertwo.composeapp.generated.resources.onboarding_third_title
import ktshwnumbertwo.composeapp.generated.resources.second_onboarding_image
import ktshwnumbertwo.composeapp.generated.resources.third_onboarding_image
import org.example.project.CommonParcelable
import org.example.project.CommonParcelize


interface OnboardingStepState {

    val nextPage: OnboardingStepState?
    fun currentState(): OnboardingPage

    @CommonParcelize
    object FirstPage : OnboardingStepState, CommonParcelable {

        override val nextPage: OnboardingStepState? = SecondPage

        override fun currentState(): OnboardingPage {
            return OnboardingPage(
                Res.string.onboarding_first_title,
                Res.string.onboarding_first_desc,
                Res.drawable.first_onboarding_image
            )
        }

    }

    @CommonParcelize
    object SecondPage : OnboardingStepState, CommonParcelable {

        override val nextPage: OnboardingStepState? = ThirdPage

        override fun currentState(): OnboardingPage {
            return OnboardingPage(
                Res.string.onboarding_second_title,
                Res.string.onboarding_second_desc,
                Res.drawable.second_onboarding_image
            )
        }

    }

    @CommonParcelize
    object ThirdPage : OnboardingStepState, CommonParcelable {

        override val nextPage: OnboardingStepState? = null

        override fun currentState(): OnboardingPage {
            return OnboardingPage(
                Res.string.onboarding_third_title,
                Res.string.onboarding_third_desc,
                Res.drawable.third_onboarding_image
            )
        }
    }
}