package com.br.zup.keymanager.pix

import com.br.zup.keymanager.pix.register.RegisterKeyRequest
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidPixKeyValidator::class])
annotation class ValidPixKey (
    val message: String = "chave Pix inválida",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

@Singleton
class ValidPixKeyValidator: javax.validation.ConstraintValidator<ValidPixKey, RegisterKeyRequest>  {

    override fun isValid(newPixKey: RegisterKeyRequest?,
                         context: ConstraintValidatorContext?): Boolean {
        if(newPixKey?.keyType == null) {
            return true
        }

        val valid = newPixKey.keyType.validate(newPixKey.key)
        if (!valid) {
            context?.let {
                it.disableDefaultConstraintViolation()
                it.buildConstraintViolationWithTemplate(context.defaultConstraintMessageTemplate)
                    .addPropertyNode("key").addConstraintViolation()
            }

        }

        return valid
    }

}
