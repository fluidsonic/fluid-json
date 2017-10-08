package com.github.fluidsonic.fluid.json

import org.apiguardian.api.API


@API(status = API.Status.EXPERIMENTAL)
internal interface JSONCodec<Value : Any, in Context : JSONCoderContext>
	: JSONDecoderCodec<Value, Context>, JSONEncoderCodec<Value, Context>
