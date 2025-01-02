package com.student.cryptoanalitics.domain.usecases

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GeminiUseCase(private val gemini: GenerativeModel) {

    fun requestGemini(coin: CryptoCoinModel): Flow<GenerateContentResponse> {
        return flow {
            emit(gemini.generateContent(ANALISE_CRYPTO_COIN + coin))
        }
    }

    companion object {
        const val DEFAULT_RESPONSE = "Виникла помилка під час аналізу"
        private const val ANALISE_CRYPTO_COIN =
            "Проаналізуй показники криптовалюти та надай короткий висновок і рекомендацію у вигляді текстового блоку. Обмеж відповідь 250 символами. Використовуй пробіли, відступи або перенесення на новий рядок для зрозумілого форматування тексту, інші символи заборонені. Уникай зайвого тексту та складних конструкцій.\n" +
                    "Відповідь у форматі:\n" +
                    "\nВисновок: тут інформація про твій аналіз без символів *,# і тд" +
                    "\nРекомендації: тут інформація про твою ррекомендацію без символів *,# і тд" +
                    "\nКриптовалюта по який робити аналіз:"
    }
}