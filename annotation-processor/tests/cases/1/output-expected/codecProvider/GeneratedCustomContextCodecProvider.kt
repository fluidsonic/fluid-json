package codecProvider

import AutomaticRootJSONCodec
import CustomRootJSONCodec
import DefaultRootJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodecProvider
import kotlin.Suppress
import kotlin.reflect.KClass

private object GeneratedCustomContextCodecProvider : CustomContextCodecProvider,
		JSONCodecProvider<CustomCodingContext> by JSONCodecProvider(AutomaticRootJSONCodec,
		CustomRootJSONCodec, DefaultRootJSONCodec, customProperties.CustomContextJSONCodec,
		customProperties.DifferentPackageJSONCodec, customProperties.SamePackageJSONCodec,
		externalType.ExternalPairCodec, externalType.KT30280JSONCodec, json.classes.ClassJSONCodec,
		json.classes.DataClassJSONCodec, json.classes.GenericClassJSONCodec,
		json.classes.InlineClassJSONCodec, json.classes.ObjectJSONCodec,
		json.codecName.AutomaticJSONCodec, json.codecName.CustomizedJSONCodec,
		json.codecName.DefaultJSONCodec, json.codecPackageName.AutomaticJSONCodec,
		json.codecPackageName.DefaultJSONCodec, json.codecPackageName.customized.CustomJSONCodec,
		json.codecVisibility.AutomaticInternalForContainedClass_ContainedClassJSONCodec,
		json.codecVisibility.AutomaticInternalJSONCodec, json.codecVisibility.AutomaticPublicJSONCodec,
		json.codecVisibility.DefaultInternalJSONCodec, json.codecVisibility.InternalJSONCodec,
		json.codecVisibility.PublicJSONCodec, json.decoding.AnnotatedConstructorJSONCodec,
		json.decoding.AutomaticAnnotatedConstructorJSONCodec, json.decoding.AutomaticObjectJSONCodec,
		json.decoding.AutomaticPrimaryConstructorJSONCodec,
		json.decoding.AutomaticSecondaryConstructorPrimaryExcludedJSONCodec,
		json.decoding.AutomaticSecondaryConstructorPrimaryInaccessibleJSONCodec,
		json.decoding.AutomaticSecondaryConstructorPrimaryNotPresentJSONCodec,
		json.decoding.DefaultAnnotatedConstructorJSONCodec, json.decoding.DefaultObjectJSONCodec,
		json.decoding.DefaultPrimaryConstructorJSONCodec,
		json.decoding.DefaultSecondaryConstructorPrimaryExcludedJSONCodec,
		json.decoding.DefaultSecondaryConstructorPrimaryInaccessibleJSONCodec,
		json.decoding.DefaultSecondaryConstructorPrimaryNotPresentJSONCodec, json.decoding.NoneJSONCodec,
		json.decoding.PrimaryConstructorJSONCodec, json.encoding.AllPropertiesJSONCodec,
		json.encoding.AnnotatedPropertiesJSONCodec, json.encoding.AutomaticJSONCodec,
		json.encoding.DefaultJSONCodec, json.encoding.NoneJSONCodec,
		json.representation.AutomaticSingleValueJSONCodec,
		json.representation.AutomaticStructuredJSONCodec, json.representation.DefaultSingleValueJSONCodec,
		json.representation.DefaultStructuredJSONCodec, json.representation.SingleValueGenericJSONCodec,
		json.representation.SingleValueJSONCodec, json.representation.SingleValueNullableJSONCodec,
		json.representation.StructuredJSONCodec, json.representation.StructuredValueGenericJSONCodec,
		property.serializedName.AutomaticJSONCodec, property.serializedName.CustomJSONCodec,
		property.serializedName.DefaultJSONCodec)

@Suppress("UNUSED_PARAMETER")
fun JSONCodecProvider.Companion.generated(interfaceClass: KClass<CustomContextCodecProvider>):
		CustomContextCodecProvider = GeneratedCustomContextCodecProvider
