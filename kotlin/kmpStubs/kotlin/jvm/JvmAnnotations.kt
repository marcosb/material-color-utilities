// No-op stubs for JVM-specific annotations on non-JVM KMP targets.
// These are added only to non-JVM source sets so they never conflict
// with the real kotlin.jvm annotations on Android/JVM.
@file:Suppress("PACKAGE_OR_CLASSIFIER_REDECLARATION")

package kotlin.jvm

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
)
@Retention(AnnotationRetention.RUNTIME)
annotation class JvmStatic
