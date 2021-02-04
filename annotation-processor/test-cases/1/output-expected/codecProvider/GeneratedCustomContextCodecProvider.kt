package codecProvider

import AutomaticRootJsonCodec
import CustomRootJsonCodec
import DefaultRootJsonCodec
import io.fluidsonic.json.JsonCodecProvider
import kotlin.Suppress
import kotlin.reflect.KClass

private object GeneratedCustomContextCodecProvider : CustomContextCodecProvider,
		JsonCodecProvider<CustomCodingContext> by JsonCodecProvider(AutomaticRootJsonCodec,
		CustomRootJsonCodec, DefaultRootJsonCodec, customProperties.CustomContextJsonCodec,
		customProperties.DifferentPackageJsonCodec, customProperties.SamePackageJsonCodec,
		externalType.ExternalPairCodec, externalType.KT30280JsonCodec,
		externalType.KT30280PrimitiveJsonCodec, json.classes.ClassJsonCodec,
		json.classes.DataClassJsonCodec, json.classes.GenericClassJsonCodec,
		json.classes.InlineClassJsonCodec, json.classes.ObjectJsonCodec,
		json.codecName.AutomaticJsonCodec, json.codecName.CustomizedJsonCodec,
		json.codecName.DefaultJsonCodec, json.codecPackageName.AutomaticJsonCodec,
		json.codecPackageName.DefaultJsonCodec, json.codecPackageName.customized.CustomJsonCodec,
		json.codecVisibility.AutomaticInternalForContainedClass_ContainedClassJsonCodec,
		json.codecVisibility.AutomaticInternalJsonCodec, json.codecVisibility.AutomaticPublicJsonCodec,
		json.codecVisibility.DefaultInternalJsonCodec, json.codecVisibility.InternalJsonCodec,
		json.codecVisibility.PublicJsonCodec, json.decoding.AnnotatedConstructorJsonCodec,
		json.decoding.AutomaticAnnotatedConstructorJsonCodec, json.decoding.AutomaticObjectJsonCodec,
		json.decoding.AutomaticPrimaryConstructorJsonCodec,
		json.decoding.AutomaticSecondaryConstructorPrimaryExcludedJsonCodec,
		json.decoding.AutomaticSecondaryConstructorPrimaryInaccessibleJsonCodec,
		json.decoding.AutomaticSecondaryConstructorPrimaryNotPresentJsonCodec,
		json.decoding.ConstructorParameterOrderJsonCodec,
		json.decoding.DefaultAnnotatedConstructorJsonCodec, json.decoding.DefaultObjectJsonCodec,
		json.decoding.DefaultPrimaryConstructorJsonCodec,
		json.decoding.DefaultSecondaryConstructorPrimaryExcludedJsonCodec,
		json.decoding.DefaultSecondaryConstructorPrimaryInaccessibleJsonCodec,
		json.decoding.DefaultSecondaryConstructorPrimaryNotPresentJsonCodec, json.decoding.NoneJsonCodec,
		json.decoding.PrimaryConstructorJsonCodec, json.encoding.AllPropertiesJsonCodec,
		json.encoding.AnnotatedPropertiesJsonCodec, json.encoding.AutomaticJsonCodec,
		json.encoding.DefaultJsonCodec, json.encoding.NoneJsonCodec,
		json.representation.AutomaticSingleValueJsonCodec,
		json.representation.AutomaticStructuredJsonCodec, json.representation.DefaultSingleValueJsonCodec,
		json.representation.DefaultStructuredJsonCodec, json.representation.SingleValueGenericJsonCodec,
		json.representation.SingleValueJsonCodec, json.representation.SingleValueNullableJsonCodec,
		json.representation.StructuredJsonCodec, json.representation.StructuredValueGenericJsonCodec,
		property.defaultValue.AutomaticGenericJsonCodec, property.defaultValue.AutomaticJsonCodec,
		property.serializedName.AutomaticJsonCodec, property.serializedName.CustomJsonCodec,
		property.serializedName.DefaultJsonCodec)

@Suppress("UNUSED_PARAMETER")
public
		fun JsonCodecProvider.Companion.generated(interfaceClass: KClass<CustomContextCodecProvider>):
		CustomContextCodecProvider = GeneratedCustomContextCodecProvider
