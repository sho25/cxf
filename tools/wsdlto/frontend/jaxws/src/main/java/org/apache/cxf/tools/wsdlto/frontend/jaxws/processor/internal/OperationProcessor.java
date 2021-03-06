begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebParam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|jaxb
operator|.
name|JAXBUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|FaultInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|MessageInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|MessagePartInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|OperationInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|ToolConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|ToolContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|ToolException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JAnnotation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JAnnotationElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JavaInterface
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JavaMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JavaParameter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JavaReturn
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JavaType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|customization
operator|.
name|JAXWSBinding
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|internal
operator|.
name|annotator
operator|.
name|WSActionAnnotator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|internal
operator|.
name|annotator
operator|.
name|WebMethodAnnotator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|internal
operator|.
name|annotator
operator|.
name|WebResultAnnotator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|internal
operator|.
name|annotator
operator|.
name|WrapperAnnotator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|internal
operator|.
name|mapper
operator|.
name|MethodMapper
import|;
end_import

begin_class
specifier|public
class|class
name|OperationProcessor
extends|extends
name|AbstractProcessor
block|{
specifier|private
name|JavaParameter
name|wrapperRequest
decl_stmt|;
specifier|private
name|JavaParameter
name|wrapperResponse
decl_stmt|;
specifier|public
name|OperationProcessor
parameter_list|(
name|ToolContext
name|c
parameter_list|)
block|{
name|super
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|process
parameter_list|(
name|JavaInterface
name|intf
parameter_list|,
name|OperationInfo
name|operation
parameter_list|)
throws|throws
name|ToolException
block|{
name|JavaMethod
name|method
init|=
operator|new
name|MethodMapper
argument_list|()
operator|.
name|map
argument_list|(
name|operation
argument_list|)
decl_stmt|;
name|method
operator|.
name|setInterface
argument_list|(
name|intf
argument_list|)
expr_stmt|;
name|processMethod
argument_list|(
name|method
argument_list|,
name|operation
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|FaultInfo
argument_list|>
name|faults
init|=
name|operation
operator|.
name|getFaults
argument_list|()
decl_stmt|;
name|FaultProcessor
name|faultProcessor
init|=
operator|new
name|FaultProcessor
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|faultProcessor
operator|.
name|process
argument_list|(
name|method
argument_list|,
name|faults
argument_list|)
expr_stmt|;
name|method
operator|.
name|annotate
argument_list|(
operator|new
name|WSActionAnnotator
argument_list|(
name|operation
argument_list|)
argument_list|)
expr_stmt|;
name|intf
operator|.
name|addMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
name|void
name|processMethod
parameter_list|(
name|JavaMethod
name|method
parameter_list|,
name|OperationInfo
name|operation
parameter_list|)
throws|throws
name|ToolException
block|{
if|if
condition|(
name|isAsyncMethod
argument_list|(
name|method
argument_list|)
condition|)
block|{
return|return;
block|}
name|MessageInfo
name|inputMessage
init|=
name|operation
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|MessageInfo
name|outputMessage
init|=
name|operation
operator|.
name|getOutput
argument_list|()
decl_stmt|;
if|if
condition|(
name|inputMessage
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"NO_INPUT_MESSAGE"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|operation
operator|.
name|getName
argument_list|()
block|}
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
name|msg
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"INVALID_MEP"
argument_list|,
name|LOG
argument_list|,
operator|new
name|Object
index|[]
block|{
name|operation
operator|.
name|getName
argument_list|()
block|}
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
name|ParameterProcessor
name|paramProcessor
init|=
operator|new
name|ParameterProcessor
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|method
operator|.
name|clear
argument_list|()
expr_stmt|;
name|JAXWSBinding
name|opBinding
init|=
name|operation
operator|.
name|getExtensor
argument_list|(
name|JAXWSBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|JAXWSBinding
name|ptBinding
init|=
name|operation
operator|.
name|getInterface
argument_list|()
operator|.
name|getExtensor
argument_list|(
name|JAXWSBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|JAXWSBinding
name|defBinding
init|=
name|operation
operator|.
name|getInterface
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getDescription
argument_list|()
operator|.
name|getExtensor
argument_list|(
name|JAXWSBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|enableAsync
init|=
literal|false
decl_stmt|;
name|boolean
name|enableMime
init|=
literal|false
decl_stmt|;
name|boolean
name|enableWrapper
init|=
name|method
operator|.
name|isWrapperStyle
argument_list|()
decl_stmt|;
if|if
condition|(
name|defBinding
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|defBinding
operator|.
name|isSetEnableMime
argument_list|()
condition|)
block|{
name|enableMime
operator|=
name|defBinding
operator|.
name|isEnableMime
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|defBinding
operator|.
name|isSetEnableAsyncMapping
argument_list|()
condition|)
block|{
name|enableAsync
operator|=
name|defBinding
operator|.
name|isEnableAsyncMapping
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|defBinding
operator|.
name|isSetEnableWrapperStyle
argument_list|()
condition|)
block|{
name|enableWrapper
operator|=
name|defBinding
operator|.
name|isEnableWrapperStyle
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ptBinding
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ptBinding
operator|.
name|isSetEnableMime
argument_list|()
condition|)
block|{
name|enableMime
operator|=
name|ptBinding
operator|.
name|isEnableMime
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ptBinding
operator|.
name|isSetEnableAsyncMapping
argument_list|()
condition|)
block|{
name|enableAsync
operator|=
name|ptBinding
operator|.
name|isEnableAsyncMapping
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ptBinding
operator|.
name|isSetEnableWrapperStyle
argument_list|()
condition|)
block|{
name|enableWrapper
operator|=
name|ptBinding
operator|.
name|isEnableWrapperStyle
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|opBinding
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|opBinding
operator|.
name|isSetEnableMime
argument_list|()
condition|)
block|{
name|enableMime
operator|=
name|opBinding
operator|.
name|isEnableMime
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|opBinding
operator|.
name|isSetEnableAsyncMapping
argument_list|()
condition|)
block|{
name|enableAsync
operator|=
name|opBinding
operator|.
name|isEnableAsyncMapping
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|opBinding
operator|.
name|isSetEnableWrapperStyle
argument_list|()
condition|)
block|{
name|enableWrapper
operator|=
name|opBinding
operator|.
name|isEnableWrapperStyle
argument_list|()
expr_stmt|;
block|}
block|}
name|enableWrapper
operator|=
name|checkEnableWrapper
argument_list|(
name|enableWrapper
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|enableAsync
operator|=
name|checkEnableAsync
argument_list|(
name|enableAsync
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|enableMime
operator|=
name|checkEnableMime
argument_list|(
name|enableMime
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|method
operator|.
name|setWrapperStyle
argument_list|(
name|enableWrapper
operator|&&
name|method
operator|.
name|isWrapperStyle
argument_list|()
argument_list|)
expr_stmt|;
name|paramProcessor
operator|.
name|process
argument_list|(
name|method
argument_list|,
name|inputMessage
argument_list|,
name|outputMessage
argument_list|,
name|operation
operator|.
name|getParameterOrdering
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|method
operator|.
name|isWrapperStyle
argument_list|()
condition|)
block|{
name|setWrapper
argument_list|(
name|operation
argument_list|)
expr_stmt|;
name|method
operator|.
name|annotate
argument_list|(
operator|new
name|WrapperAnnotator
argument_list|(
name|wrapperRequest
argument_list|,
name|wrapperResponse
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|method
operator|.
name|annotate
argument_list|(
operator|new
name|WebMethodAnnotator
argument_list|()
argument_list|)
expr_stmt|;
name|method
operator|.
name|annotate
argument_list|(
operator|new
name|WebResultAnnotator
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|method
operator|.
name|isOneWay
argument_list|()
operator|&&
name|enableAsync
operator|&&
operator|!
name|isAddedAsycMethod
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|addAsyncMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|enableMime
condition|)
block|{
name|method
operator|.
name|setMimeEnable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|checkArray
parameter_list|(
name|String
index|[]
name|ar
parameter_list|,
name|String
name|n
parameter_list|)
block|{
if|if
condition|(
name|ar
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ar
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
for|for
control|(
name|String
name|s
range|:
name|ar
control|)
block|{
if|if
condition|(
name|s
operator|.
name|equals
argument_list|(
name|n
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|boolean
name|checkEnableMime
parameter_list|(
name|boolean
name|enableMime
parameter_list|,
name|JavaMethod
name|method
parameter_list|)
block|{
name|String
index|[]
name|o
init|=
name|context
operator|.
name|getArray
argument_list|(
name|ToolConstants
operator|.
name|CFG_MIMEMETHODS
argument_list|)
decl_stmt|;
if|if
condition|(
name|checkArray
argument_list|(
name|o
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|enableMime
return|;
block|}
specifier|private
name|boolean
name|checkEnableAsync
parameter_list|(
name|boolean
name|enableAsync
parameter_list|,
name|JavaMethod
name|method
parameter_list|)
block|{
name|String
index|[]
name|o
init|=
name|context
operator|.
name|getArray
argument_list|(
name|ToolConstants
operator|.
name|CFG_ASYNCMETHODS
argument_list|)
decl_stmt|;
if|if
condition|(
name|checkArray
argument_list|(
name|o
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|enableAsync
return|;
block|}
specifier|private
name|boolean
name|checkEnableWrapper
parameter_list|(
name|boolean
name|enableWrapper
parameter_list|,
name|JavaMethod
name|method
parameter_list|)
block|{
name|String
index|[]
name|o
init|=
name|context
operator|.
name|getArray
argument_list|(
name|ToolConstants
operator|.
name|CFG_BAREMETHODS
argument_list|)
decl_stmt|;
if|if
condition|(
name|checkArray
argument_list|(
name|o
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|enableWrapper
return|;
block|}
specifier|private
name|void
name|setWrapper
parameter_list|(
name|OperationInfo
name|operation
parameter_list|)
block|{
name|MessagePartInfo
name|inputPart
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|operation
operator|.
name|getInput
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|inputPart
operator|=
name|operation
operator|.
name|getInput
argument_list|()
operator|.
name|getFirstMessagePart
argument_list|()
expr_stmt|;
block|}
name|MessagePartInfo
name|outputPart
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|operation
operator|.
name|getOutput
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|outputPart
operator|=
name|operation
operator|.
name|getOutput
argument_list|()
operator|.
name|getFirstMessagePart
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|inputPart
operator|!=
literal|null
condition|)
block|{
name|wrapperRequest
operator|=
operator|new
name|JavaParameter
argument_list|()
expr_stmt|;
name|wrapperRequest
operator|.
name|setName
argument_list|(
name|ProcessorUtil
operator|.
name|resolvePartName
argument_list|(
name|inputPart
argument_list|)
argument_list|)
expr_stmt|;
name|wrapperRequest
operator|.
name|setType
argument_list|(
name|ProcessorUtil
operator|.
name|getPartType
argument_list|(
name|inputPart
argument_list|)
argument_list|)
expr_stmt|;
name|wrapperRequest
operator|.
name|setTargetNamespace
argument_list|(
name|ProcessorUtil
operator|.
name|resolvePartNamespace
argument_list|(
name|inputPart
argument_list|)
argument_list|)
expr_stmt|;
name|wrapperRequest
operator|.
name|setClassName
argument_list|(
name|ProcessorUtil
operator|.
name|getFullClzName
argument_list|(
name|inputPart
argument_list|,
name|context
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|outputPart
operator|!=
literal|null
condition|)
block|{
name|wrapperResponse
operator|=
operator|new
name|JavaParameter
argument_list|()
expr_stmt|;
name|wrapperResponse
operator|.
name|setName
argument_list|(
name|ProcessorUtil
operator|.
name|resolvePartName
argument_list|(
name|outputPart
argument_list|)
argument_list|)
expr_stmt|;
name|wrapperResponse
operator|.
name|setType
argument_list|(
name|ProcessorUtil
operator|.
name|getPartType
argument_list|(
name|outputPart
argument_list|)
argument_list|)
expr_stmt|;
name|wrapperResponse
operator|.
name|setTargetNamespace
argument_list|(
name|ProcessorUtil
operator|.
name|resolvePartNamespace
argument_list|(
name|outputPart
argument_list|)
argument_list|)
expr_stmt|;
name|wrapperResponse
operator|.
name|setClassName
argument_list|(
name|ProcessorUtil
operator|.
name|getFullClzName
argument_list|(
name|outputPart
argument_list|,
name|context
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|isAsyncMethod
parameter_list|(
name|JavaMethod
name|method
parameter_list|)
block|{
if|if
condition|(
name|method
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
name|ToolConstants
operator|.
name|ASYNC_METHOD_SUFFIX
argument_list|)
operator|&&
name|method
operator|.
name|getReturn
argument_list|()
operator|!=
literal|null
operator|&&
name|method
operator|.
name|getReturn
argument_list|()
operator|.
name|getClassName
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|method
operator|.
name|getReturn
argument_list|()
operator|.
name|getClassName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Response<"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|method
operator|.
name|getParameterCount
argument_list|()
operator|>
literal|0
operator|&&
name|method
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
name|method
operator|.
name|getParameterCount
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|getClassName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"AsyncHandler<"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|void
name|addAsyncMethod
parameter_list|(
name|JavaMethod
name|method
parameter_list|)
throws|throws
name|ToolException
block|{
name|addPollingMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|addCallbackMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|method
operator|.
name|getInterface
argument_list|()
operator|.
name|addImport
argument_list|(
literal|"javax.xml.ws.AsyncHandler"
argument_list|)
expr_stmt|;
name|method
operator|.
name|getInterface
argument_list|()
operator|.
name|addImport
argument_list|(
literal|"java.util.concurrent.Future"
argument_list|)
expr_stmt|;
name|method
operator|.
name|getInterface
argument_list|()
operator|.
name|addImport
argument_list|(
literal|"javax.xml.ws.Response"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addCallbackMethod
parameter_list|(
name|JavaMethod
name|method
parameter_list|)
throws|throws
name|ToolException
block|{
name|JavaMethod
name|callbackMethod
init|=
operator|new
name|JavaMethod
argument_list|(
name|method
operator|.
name|getInterface
argument_list|()
argument_list|)
decl_stmt|;
name|callbackMethod
operator|.
name|setAsync
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|callbackMethod
operator|.
name|setName
argument_list|(
name|method
operator|.
name|getName
argument_list|()
operator|+
name|ToolConstants
operator|.
name|ASYNC_METHOD_SUFFIX
argument_list|)
expr_stmt|;
name|callbackMethod
operator|.
name|setStyle
argument_list|(
name|method
operator|.
name|getStyle
argument_list|()
argument_list|)
expr_stmt|;
name|callbackMethod
operator|.
name|setWrapperStyle
argument_list|(
name|method
operator|.
name|isWrapperStyle
argument_list|()
argument_list|)
expr_stmt|;
name|callbackMethod
operator|.
name|setSoapAction
argument_list|(
name|method
operator|.
name|getSoapAction
argument_list|()
argument_list|)
expr_stmt|;
name|callbackMethod
operator|.
name|setOperationName
argument_list|(
name|method
operator|.
name|getOperationName
argument_list|()
argument_list|)
expr_stmt|;
name|JavaReturn
name|future
init|=
operator|new
name|JavaReturn
argument_list|()
decl_stmt|;
name|future
operator|.
name|setClassName
argument_list|(
literal|"Future<?>"
argument_list|)
expr_stmt|;
name|callbackMethod
operator|.
name|setReturn
argument_list|(
name|future
argument_list|)
expr_stmt|;
comment|// REVISIT: test the operation name in the annotation
name|callbackMethod
operator|.
name|annotate
argument_list|(
operator|new
name|WebMethodAnnotator
argument_list|()
argument_list|)
expr_stmt|;
name|callbackMethod
operator|.
name|addAnnotation
argument_list|(
literal|"ResponseWrapper"
argument_list|,
name|method
operator|.
name|getAnnotationMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"ResponseWrapper"
argument_list|)
argument_list|)
expr_stmt|;
name|callbackMethod
operator|.
name|addAnnotation
argument_list|(
literal|"RequestWrapper"
argument_list|,
name|method
operator|.
name|getAnnotationMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"RequestWrapper"
argument_list|)
argument_list|)
expr_stmt|;
name|callbackMethod
operator|.
name|addAnnotation
argument_list|(
literal|"SOAPBinding"
argument_list|,
name|method
operator|.
name|getAnnotationMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"SOAPBinding"
argument_list|)
argument_list|)
expr_stmt|;
name|boolean
name|convertOutToAsync
init|=
operator|!
name|method
operator|.
name|isWrapperStyle
argument_list|()
operator|&&
literal|"void"
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getReturn
argument_list|()
operator|.
name|getClassName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|asyncCname
init|=
literal|null
decl_stmt|;
for|for
control|(
name|JavaParameter
name|param
range|:
name|method
operator|.
name|getParameters
argument_list|()
control|)
block|{
if|if
condition|(
name|convertOutToAsync
condition|)
block|{
if|if
condition|(
name|param
operator|.
name|isHolder
argument_list|()
condition|)
block|{
if|if
condition|(
name|param
operator|.
name|isINOUT
argument_list|()
condition|)
block|{
name|JavaParameter
name|p2
init|=
operator|new
name|JavaParameter
argument_list|()
decl_stmt|;
name|p2
operator|.
name|setName
argument_list|(
name|param
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|p2
operator|.
name|setClassName
argument_list|(
name|param
operator|.
name|getHolderName
argument_list|()
argument_list|)
expr_stmt|;
name|p2
operator|.
name|setStyle
argument_list|(
name|JavaType
operator|.
name|Style
operator|.
name|IN
argument_list|)
expr_stmt|;
name|callbackMethod
operator|.
name|addParameter
argument_list|(
name|p2
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|s
range|:
name|param
operator|.
name|getAnnotationTags
argument_list|()
control|)
block|{
name|JAnnotation
name|ann
init|=
name|param
operator|.
name|getAnnotation
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|p2
operator|.
name|addAnnotation
argument_list|(
name|s
argument_list|,
name|ann
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|param
operator|.
name|isHeader
argument_list|()
operator|&&
name|asyncCname
operator|==
literal|null
condition|)
block|{
name|asyncCname
operator|=
name|param
operator|.
name|getClassName
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|callbackMethod
operator|.
name|addParameter
argument_list|(
name|param
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|callbackMethod
operator|.
name|addParameter
argument_list|(
name|param
argument_list|)
expr_stmt|;
block|}
block|}
name|JavaParameter
name|asyncHandler
init|=
operator|new
name|JavaParameter
argument_list|()
decl_stmt|;
name|asyncHandler
operator|.
name|setName
argument_list|(
literal|"asyncHandler"
argument_list|)
expr_stmt|;
name|asyncHandler
operator|.
name|setCallback
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|asyncHandler
operator|.
name|setClassName
argument_list|(
name|getAsyncClassName
argument_list|(
name|method
argument_list|,
literal|"AsyncHandler"
argument_list|,
name|asyncCname
argument_list|)
argument_list|)
expr_stmt|;
name|asyncHandler
operator|.
name|setStyle
argument_list|(
name|JavaType
operator|.
name|Style
operator|.
name|IN
argument_list|)
expr_stmt|;
name|callbackMethod
operator|.
name|addParameter
argument_list|(
name|asyncHandler
argument_list|)
expr_stmt|;
name|JAnnotation
name|asyncHandlerAnnotation
init|=
operator|new
name|JAnnotation
argument_list|(
name|WebParam
operator|.
name|class
argument_list|)
decl_stmt|;
name|asyncHandlerAnnotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"name"
argument_list|,
literal|"asyncHandler"
argument_list|)
argument_list|)
expr_stmt|;
name|asyncHandlerAnnotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"targetNamespace"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|asyncHandler
operator|.
name|addAnnotation
argument_list|(
literal|"WebParam"
argument_list|,
name|asyncHandlerAnnotation
argument_list|)
expr_stmt|;
name|method
operator|.
name|getInterface
argument_list|()
operator|.
name|addImport
argument_list|(
literal|"javax.jws.WebParam"
argument_list|)
expr_stmt|;
name|method
operator|.
name|getInterface
argument_list|()
operator|.
name|addMethod
argument_list|(
name|callbackMethod
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addPollingMethod
parameter_list|(
name|JavaMethod
name|method
parameter_list|)
throws|throws
name|ToolException
block|{
name|JavaMethod
name|pollingMethod
init|=
operator|new
name|JavaMethod
argument_list|(
name|method
operator|.
name|getInterface
argument_list|()
argument_list|)
decl_stmt|;
name|pollingMethod
operator|.
name|setAsync
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|pollingMethod
operator|.
name|setName
argument_list|(
name|method
operator|.
name|getName
argument_list|()
operator|+
name|ToolConstants
operator|.
name|ASYNC_METHOD_SUFFIX
argument_list|)
expr_stmt|;
name|pollingMethod
operator|.
name|setStyle
argument_list|(
name|method
operator|.
name|getStyle
argument_list|()
argument_list|)
expr_stmt|;
name|pollingMethod
operator|.
name|setWrapperStyle
argument_list|(
name|method
operator|.
name|isWrapperStyle
argument_list|()
argument_list|)
expr_stmt|;
name|pollingMethod
operator|.
name|setSoapAction
argument_list|(
name|method
operator|.
name|getSoapAction
argument_list|()
argument_list|)
expr_stmt|;
name|pollingMethod
operator|.
name|setOperationName
argument_list|(
name|method
operator|.
name|getOperationName
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|convertOutToAsync
init|=
operator|!
name|method
operator|.
name|isWrapperStyle
argument_list|()
operator|&&
literal|"void"
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getReturn
argument_list|()
operator|.
name|getClassName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|asyncCname
init|=
literal|null
decl_stmt|;
for|for
control|(
name|JavaParameter
name|param
range|:
name|method
operator|.
name|getParameters
argument_list|()
control|)
block|{
if|if
condition|(
name|convertOutToAsync
condition|)
block|{
if|if
condition|(
name|param
operator|.
name|isHolder
argument_list|()
condition|)
block|{
if|if
condition|(
name|param
operator|.
name|isINOUT
argument_list|()
condition|)
block|{
name|JavaParameter
name|p2
init|=
operator|new
name|JavaParameter
argument_list|()
decl_stmt|;
name|p2
operator|.
name|setName
argument_list|(
name|param
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|p2
operator|.
name|setClassName
argument_list|(
name|param
operator|.
name|getHolderName
argument_list|()
argument_list|)
expr_stmt|;
name|p2
operator|.
name|setStyle
argument_list|(
name|JavaType
operator|.
name|Style
operator|.
name|IN
argument_list|)
expr_stmt|;
name|pollingMethod
operator|.
name|addParameter
argument_list|(
name|p2
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|s
range|:
name|param
operator|.
name|getAnnotationTags
argument_list|()
control|)
block|{
name|JAnnotation
name|ann
init|=
name|param
operator|.
name|getAnnotation
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|p2
operator|.
name|addAnnotation
argument_list|(
name|s
argument_list|,
name|ann
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|param
operator|.
name|isHeader
argument_list|()
operator|&&
name|asyncCname
operator|==
literal|null
condition|)
block|{
name|asyncCname
operator|=
name|param
operator|.
name|getClassName
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|pollingMethod
operator|.
name|addParameter
argument_list|(
name|param
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|pollingMethod
operator|.
name|addParameter
argument_list|(
name|param
argument_list|)
expr_stmt|;
block|}
block|}
name|JavaReturn
name|response
init|=
operator|new
name|JavaReturn
argument_list|()
decl_stmt|;
name|response
operator|.
name|setClassName
argument_list|(
name|getAsyncClassName
argument_list|(
name|method
argument_list|,
literal|"Response"
argument_list|,
name|asyncCname
argument_list|)
argument_list|)
expr_stmt|;
name|pollingMethod
operator|.
name|setReturn
argument_list|(
name|response
argument_list|)
expr_stmt|;
comment|// REVISIT: test the operation name in the annotation
name|pollingMethod
operator|.
name|annotate
argument_list|(
operator|new
name|WebMethodAnnotator
argument_list|()
argument_list|)
expr_stmt|;
name|pollingMethod
operator|.
name|addAnnotation
argument_list|(
literal|"RequestWrapper"
argument_list|,
name|method
operator|.
name|getAnnotationMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"RequestWrapper"
argument_list|)
argument_list|)
expr_stmt|;
name|pollingMethod
operator|.
name|addAnnotation
argument_list|(
literal|"ResponseWrapper"
argument_list|,
name|method
operator|.
name|getAnnotationMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"ResponseWrapper"
argument_list|)
argument_list|)
expr_stmt|;
name|pollingMethod
operator|.
name|addAnnotation
argument_list|(
literal|"SOAPBinding"
argument_list|,
name|method
operator|.
name|getAnnotationMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"SOAPBinding"
argument_list|)
argument_list|)
expr_stmt|;
name|method
operator|.
name|getInterface
argument_list|()
operator|.
name|addMethod
argument_list|(
name|pollingMethod
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getAsyncClassName
parameter_list|(
name|JavaMethod
name|method
parameter_list|,
name|String
name|clzName
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|String
name|response
init|=
name|name
decl_stmt|;
if|if
condition|(
name|response
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|wrapperResponse
operator|!=
literal|null
condition|)
block|{
name|response
operator|=
name|wrapperResponse
operator|.
name|getClassName
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|response
operator|=
name|method
operator|.
name|getReturn
argument_list|()
operator|.
name|getClassName
argument_list|()
expr_stmt|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|mappedClass
init|=
name|JAXBUtils
operator|.
name|holderClass
argument_list|(
name|response
argument_list|)
decl_stmt|;
if|if
condition|(
name|mappedClass
operator|!=
literal|null
condition|)
block|{
name|response
operator|=
name|mappedClass
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|clzName
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'<'
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"void"
operator|.
name|equals
argument_list|(
name|response
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'?'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|'>'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|boolean
name|isAddedAsycMethod
parameter_list|(
name|JavaMethod
name|method
parameter_list|)
block|{
name|List
argument_list|<
name|JavaMethod
argument_list|>
name|jmethods
init|=
name|method
operator|.
name|getInterface
argument_list|()
operator|.
name|getMethods
argument_list|()
decl_stmt|;
name|int
name|counter
init|=
literal|0
decl_stmt|;
for|for
control|(
name|JavaMethod
name|jm
range|:
name|jmethods
control|)
block|{
if|if
condition|(
name|jm
operator|.
name|getOperationName
argument_list|()
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getOperationName
argument_list|()
argument_list|)
condition|)
block|{
name|counter
operator|++
expr_stmt|;
block|}
block|}
return|return
name|counter
operator|>
literal|1
condition|?
literal|true
else|:
literal|false
return|;
block|}
block|}
end_class

end_unit

