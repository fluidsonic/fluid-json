package com.github.fluidsonic.fluid.json.annotationprocessor

import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.lang.model.type.TypeVariable


private fun StringBuilder.appendJvmMethodNameSignature(name: String) = apply {
	append(name)
}


private fun StringBuilder.appendJvmMethodSignature(name: String, parameterTypes: List<TypeMirror>, returnType: TypeMirror) = apply {
	appendJvmMethodNameSignature(name)
	appendJvmMethodTypeSignature(parameterTypes = parameterTypes, returnType = returnType)
}


private fun StringBuilder.appendJvmMethodTypeSignature(parameterTypes: List<TypeMirror>, returnType: TypeMirror) = apply {
	append('(')
	parameterTypes.forEach { appendJvmTypeSignature(it) }
	append(')')
	appendJvmTypeSignature(returnType)
}


private fun StringBuilder.appendJvmTypeNameSignature(type: DeclaredType) = apply {
	val element = type.asElement() as TypeElement

	var packageName = ""
	val enclosingClassNames = mutableListOf<String>()

	var enclosingElement = element.enclosingElement
	while (enclosingElement != null) {
		if (enclosingElement is PackageElement) {
			packageName = enclosingElement.qualifiedName.toString()
			break
		}
		else
			enclosingClassNames += enclosingElement.simpleName.toString()

		enclosingElement = enclosingElement.enclosingElement
	}

	enclosingClassNames.reverse()

	if (packageName.isNotEmpty()) {
		append(packageName.replace('.', '/'))
		append('/')
	}

	for (name in enclosingClassNames) {
		append(name)
		append('$')
	}

	append(element.simpleName)
}


private fun StringBuilder.appendJvmTypeSignature(type: TypeMirror): StringBuilder = apply {
	when (val kind = type.kind) {
		TypeKind.ARRAY -> append('[').appendJvmTypeSignature((type as ArrayType).componentType)
		TypeKind.BOOLEAN -> append('Z')
		TypeKind.BYTE -> append('B')
		TypeKind.CHAR -> append('C')
		TypeKind.DECLARED -> append('L').appendJvmTypeNameSignature(type as DeclaredType).append(';')
		TypeKind.DOUBLE -> append('D')
		TypeKind.FLOAT -> append('F')
		TypeKind.INT -> append('I')
		TypeKind.LONG -> append('J')
		TypeKind.SHORT -> append('S')
		TypeKind.VOID -> append('V')
		TypeKind.TYPEVAR -> appendJvmTypeSignature((type as TypeVariable).upperBound)
		else -> error("unsupported type in method signature: $kind ($type)")
	}
}


internal fun jvmMethodSignature(name: String, parameterTypes: List<TypeMirror>, returnType: TypeMirror) =
	StringBuilder().appendJvmMethodSignature(name = name, parameterTypes = parameterTypes, returnType = returnType).toString()
