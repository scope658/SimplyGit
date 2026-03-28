package org.example.project.onboarding.presentation

import kotlinx.serialization.Serializable
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

@Serializable
sealed interface OnboardingStepState {

    val nextPage: OnboardingStepState?
    fun currentState(): OnboardingPage


    @Serializable
    object FirstPage : OnboardingStepState {

        override val nextPage: OnboardingStepState? = SecondPage

        override fun currentState(): OnboardingPage {
            return OnboardingPage(
                Res.string.onboarding_first_title,
                Res.string.onboarding_first_desc,
                Res.drawable.first_onboarding_image
            )
        }

    }

    @Serializable
    object SecondPage : OnboardingStepState {

        override val nextPage: OnboardingStepState? = ThirdPage

        override fun currentState(): OnboardingPage {
            return OnboardingPage(
                Res.string.onboarding_second_title,
                Res.string.onboarding_second_desc,
                Res.drawable.second_onboarding_image
            )
        }

    }

    @Serializable
    object ThirdPage : OnboardingStepState {

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
