package io.mustelidae.smallclawedotter.utils

import org.reflections.ReflectionUtils
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

fun invokeAny(clazz: Any, arg: String, data: Any): Any {
    val props = clazz::class.memberProperties.find { it.name == arg }
    props!!.isAccessible = true
    if (props is KMutableProperty<*>) {
        props.setter.call(clazz, data)
    }
    return clazz
}

fun invokeFunc(clazz: KClass<*>, functionName: String): KFunction<*>? {
    val func = clazz.declaredMemberFunctions.find { it.name == functionName }
    func?.let {
        it.isAccessible = true
    }
    return func
}

fun invokeId(clazz: Any, hasId: Boolean = true): Any {
    if (hasId.not())
        return clazz

    val props = clazz::class.memberProperties.find { it.name == "id" }
    props!!.isAccessible = true
    if (props is KMutableProperty<*>) {
        props.setter.call(clazz, FixtureID.inc())
    }
    return clazz
}

fun setField(obj: Any, filedName: String, value: Any) {
    val fields = ReflectionUtils.getAllFields(obj.javaClass)
    for (field in fields) {
        if (field.name == filedName) {
            field.isAccessible = true
            field.set(obj, value)
        }
    }
}
