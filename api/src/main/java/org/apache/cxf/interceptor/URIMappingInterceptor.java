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
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|Map
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|logging
operator|.
name|LogUtils
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
name|util
operator|.
name|CollectionUtils
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
name|util
operator|.
name|PrimitiveUtils
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
name|util
operator|.
name|StringUtils
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
name|util
operator|.
name|XMLSchemaQNames
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
name|endpoint
operator|.
name|Endpoint
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
name|message
operator|.
name|Exchange
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
name|message
operator|.
name|Message
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
name|message
operator|.
name|MessageContentsList
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
name|message
operator|.
name|MessageUtils
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
name|phase
operator|.
name|Phase
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
name|Service
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
name|invoker
operator|.
name|MethodDispatcher
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
name|BindingInfo
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
name|BindingOperationInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceModelUtil
import|;
end_import

begin_class
specifier|public
class|class
name|URIMappingInterceptor
extends|extends
name|AbstractInDatabindingInterceptor
block|{
specifier|public
specifier|static
specifier|final
name|String
name|URIMAPPING_SKIP
init|=
name|URIMappingInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".skip"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|URIMappingInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
name|URIMappingInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|UNMARSHAL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|String
name|method
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Invoking HTTP method "
operator|+
name|method
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|isGET
argument_list|(
name|message
argument_list|)
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"URIMappingInterceptor can only handle HTTP GET, not HTTP "
operator|+
name|method
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
if|if
condition|(
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|URIMAPPING_SKIP
argument_list|,
literal|false
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
name|opName
init|=
name|getOperationName
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"URIMappingInterceptor get operation: "
operator|+
name|opName
argument_list|)
expr_stmt|;
block|}
name|BindingOperationInfo
name|op
init|=
name|ServiceModelUtil
operator|.
name|getOperation
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
name|opName
argument_list|)
decl_stmt|;
if|if
condition|(
name|op
operator|==
literal|null
operator|||
name|opName
operator|==
literal|null
operator|||
name|op
operator|.
name|getName
argument_list|()
operator|==
literal|null
operator|||
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|op
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|||
operator|!
name|opName
operator|.
name|equals
argument_list|(
name|op
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|NO_VALIDATE_PARTS
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
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
literal|"NO_OPERATION_PATH"
argument_list|,
name|LOG
argument_list|,
name|opName
argument_list|,
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PATH_INFO
argument_list|)
argument_list|)
argument_list|)
throw|;
block|}
name|MessageContentsList
name|params
init|=
operator|new
name|MessageContentsList
argument_list|()
decl_stmt|;
name|params
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|params
argument_list|)
expr_stmt|;
if|if
condition|(
name|op
operator|==
literal|null
condition|)
block|{
name|op
operator|=
name|findAnyOp
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|op
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|op
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|op
argument_list|)
expr_stmt|;
name|MessageContentsList
name|params
init|=
name|getParameters
argument_list|(
name|message
argument_list|,
name|op
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|BindingOperationInfo
name|findAnyOp
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Endpoint
name|ep
init|=
name|exchange
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|BindingInfo
name|service
init|=
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
decl_stmt|;
for|for
control|(
name|BindingOperationInfo
name|b
range|:
name|service
operator|.
name|getOperations
argument_list|()
control|)
block|{
if|if
condition|(
name|b
operator|.
name|getInput
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|b
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|MessagePartInfo
name|inf
init|=
name|b
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getMessagePart
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|XMLSchemaQNames
operator|.
name|XSD_ANY
operator|.
name|equals
argument_list|(
name|inf
operator|.
name|getTypeQName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|b
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|Method
name|getMethod
parameter_list|(
name|Message
name|message
parameter_list|,
name|BindingOperationInfo
name|operation
parameter_list|)
block|{
name|MethodDispatcher
name|md
init|=
operator|(
name|MethodDispatcher
operator|)
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Service
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
name|MethodDispatcher
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|md
operator|.
name|getMethod
argument_list|(
name|operation
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isFixedParameterOrder
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
comment|// Default value is false
name|Boolean
name|order
init|=
operator|(
name|Boolean
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|FIXED_PARAMETER_ORDER
argument_list|)
decl_stmt|;
return|return
name|order
operator|!=
literal|null
operator|&&
name|order
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|keepInOrder
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|OperationInfo
name|operation
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|order
parameter_list|)
block|{
if|if
condition|(
name|params
operator|==
literal|null
operator|||
name|order
operator|==
literal|null
condition|)
block|{
return|return
name|params
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|orderedParameters
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|order
control|)
block|{
name|orderedParameters
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|params
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|order
operator|.
name|size
argument_list|()
operator|!=
name|params
operator|.
name|size
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|order
operator|.
name|size
argument_list|()
operator|+
literal|" parameters definded in WSDL but found "
operator|+
name|params
operator|.
name|size
argument_list|()
operator|+
literal|" in request!"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|rest
init|=
name|CollectionUtils
operator|.
name|diff
argument_list|(
name|order
argument_list|,
name|params
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|rest
operator|!=
literal|null
operator|&&
name|rest
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Set the following parameters to null: "
operator|+
name|rest
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|String
argument_list|>
name|iter
init|=
name|rest
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|key
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|orderedParameters
operator|.
name|put
argument_list|(
name|key
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|orderedParameters
return|;
block|}
specifier|protected
name|MessageContentsList
name|getParameters
parameter_list|(
name|Message
name|message
parameter_list|,
name|BindingOperationInfo
name|operation
parameter_list|)
block|{
name|MessageContentsList
name|parameters
init|=
operator|new
name|MessageContentsList
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queries
init|=
name|getQueries
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isFixedParameterOrder
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|boolean
name|emptyQueries
init|=
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|queries
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|ServiceModelUtil
operator|.
name|getOperationInputPartNames
argument_list|(
name|operation
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
decl_stmt|;
name|queries
operator|=
name|keepInOrder
argument_list|(
name|queries
argument_list|,
name|operation
operator|.
name|getOperationInfo
argument_list|()
argument_list|,
name|names
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|emptyQueries
operator|&&
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|queries
operator|.
name|values
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|operation
operator|.
name|isUnwrappedCapable
argument_list|()
condition|)
block|{
comment|//maybe the wrapper was skipped
return|return
name|getParameters
argument_list|(
name|message
argument_list|,
name|operation
operator|.
name|getUnwrappedOperation
argument_list|()
argument_list|)
return|;
block|}
throw|throw
operator|new
name|Fault
argument_list|(
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
literal|"ORDERED_PARAM_REQUIRED"
argument_list|,
name|LOG
argument_list|,
name|names
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
name|Method
name|method
init|=
name|getMethod
argument_list|(
name|message
argument_list|,
name|operation
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|types
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|queries
operator|.
name|keySet
argument_list|()
control|)
block|{
name|MessagePartInfo
name|inf
init|=
literal|null
decl_stmt|;
for|for
control|(
name|MessagePartInfo
name|p
range|:
name|operation
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
control|)
block|{
if|if
condition|(
name|p
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|inf
operator|=
name|p
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|inf
operator|==
literal|null
operator|&&
name|operation
operator|.
name|isUnwrappedCapable
argument_list|()
condition|)
block|{
for|for
control|(
name|MessagePartInfo
name|p
range|:
name|operation
operator|.
name|getUnwrappedOperation
argument_list|()
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
control|)
block|{
if|if
condition|(
name|p
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|inf
operator|=
name|p
expr_stmt|;
break|break;
block|}
block|}
block|}
name|int
name|idx
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|inf
operator|!=
literal|null
condition|)
block|{
name|idx
operator|=
name|inf
operator|.
name|getIndex
argument_list|()
expr_stmt|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|types
index|[
name|idx
index|]
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"URIMappingInterceptor MessagePartInfo NULL "
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Fault
argument_list|(
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
literal|"NO_PART_FOUND"
argument_list|,
name|LOG
argument_list|,
literal|"index: "
operator|+
name|idx
operator|+
literal|" on key "
operator|+
name|key
argument_list|)
argument_list|)
throw|;
block|}
comment|// TODO check the parameter name here
name|Object
name|param
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isPrimitive
argument_list|()
operator|&&
name|queries
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|param
operator|=
name|PrimitiveUtils
operator|.
name|read
argument_list|(
name|queries
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|param
operator|=
name|readType
argument_list|(
name|queries
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
name|parameters
operator|.
name|set
argument_list|(
name|idx
argument_list|,
name|param
argument_list|)
expr_stmt|;
name|idx
operator|=
name|parameters
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
return|return
name|parameters
return|;
block|}
specifier|private
name|Date
name|parseDate
parameter_list|(
name|String
name|value
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|SimpleDateFormat
name|sdf
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|length
argument_list|()
operator|==
literal|10
condition|)
block|{
name|sdf
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|.
name|length
argument_list|()
operator|==
literal|19
condition|)
block|{
name|sdf
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd'T'HH:mm:ss"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|.
name|length
argument_list|()
operator|==
literal|23
condition|)
block|{
name|sdf
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd'T'HH:mm:ss.SSS"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|.
name|length
argument_list|()
operator|==
literal|25
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|value
operator|.
name|length
argument_list|()
operator|-
literal|3
argument_list|)
operator|+
name|value
operator|.
name|substring
argument_list|(
name|value
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|,
name|value
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|sdf
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd'T'HH:mm:ssZZZZZ"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|.
name|length
argument_list|()
operator|==
literal|29
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|value
operator|.
name|length
argument_list|()
operator|-
literal|3
argument_list|)
operator|+
name|value
operator|.
name|substring
argument_list|(
name|value
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|,
name|value
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|sdf
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to create "
operator|+
name|type
operator|+
literal|" out of '"
operator|+
name|value
operator|+
literal|"'"
argument_list|)
throw|;
block|}
try|try
block|{
return|return
name|sdf
operator|.
name|parse
argument_list|(
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to create "
operator|+
name|type
operator|+
literal|" out of '"
operator|+
name|value
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
specifier|private
name|Object
name|readType
parameter_list|(
name|String
name|value
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|ret
init|=
name|value
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
comment|// let null be null regardless of target type
block|}
elseif|else
if|if
condition|(
name|Integer
operator|.
name|class
operator|==
name|type
condition|)
block|{
name|ret
operator|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Byte
operator|.
name|class
operator|==
name|type
condition|)
block|{
name|ret
operator|=
name|Byte
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Short
operator|.
name|class
operator|==
name|type
condition|)
block|{
name|ret
operator|=
name|Short
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Long
operator|.
name|class
operator|==
name|type
condition|)
block|{
name|ret
operator|=
name|Long
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Float
operator|.
name|class
operator|==
name|type
condition|)
block|{
name|ret
operator|=
name|Float
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Double
operator|.
name|class
operator|==
name|type
condition|)
block|{
name|ret
operator|=
name|Double
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Boolean
operator|.
name|class
operator|==
name|type
condition|)
block|{
name|ret
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Character
operator|.
name|class
operator|==
name|type
condition|)
block|{
name|ret
operator|=
name|value
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|!=
literal|null
operator|&&
name|type
operator|.
name|isEnum
argument_list|()
condition|)
block|{
try|try
block|{
name|ret
operator|=
name|type
operator|.
name|getMethod
argument_list|(
literal|"valueOf"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to create "
operator|+
name|type
operator|+
literal|" out of '"
operator|+
name|value
operator|+
literal|"'"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to create "
operator|+
name|type
operator|+
literal|" out of '"
operator|+
name|value
operator|+
literal|"'"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to create "
operator|+
name|type
operator|+
literal|" out of '"
operator|+
name|value
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|java
operator|.
name|util
operator|.
name|Date
operator|.
name|class
operator|==
name|type
condition|)
block|{
name|ret
operator|=
name|parseDate
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Calendar
operator|.
name|class
operator|==
name|type
condition|)
block|{
name|ret
operator|=
name|Calendar
operator|.
name|getInstance
argument_list|()
expr_stmt|;
operator|(
operator|(
name|Calendar
operator|)
name|ret
operator|)
operator|.
name|setTime
argument_list|(
name|parseDate
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|private
name|String
name|uriDecode
parameter_list|(
name|String
name|query
parameter_list|)
block|{
try|try
block|{
name|query
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|query
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
name|query
operator|+
literal|" can not be decoded: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|query
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getQueries
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queries
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|String
name|query
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|QUERY_STRING
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|query
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|parts
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|query
operator|.
name|split
argument_list|(
literal|"&"
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
if|if
condition|(
name|part
operator|.
name|contains
argument_list|(
literal|"="
argument_list|)
condition|)
block|{
name|String
index|[]
name|keyValue
init|=
name|part
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyValue
operator|.
name|length
operator|>=
literal|2
condition|)
block|{
name|queries
operator|.
name|put
argument_list|(
name|keyValue
index|[
literal|0
index|]
argument_list|,
name|uriDecode
argument_list|(
name|keyValue
index|[
literal|1
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|queries
return|;
block|}
name|String
name|rest
init|=
name|getRest
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|parts
init|=
name|StringUtils
operator|.
name|getParts
argument_list|(
name|rest
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|parts
operator|.
name|size
argument_list|()
condition|;
name|i
operator|+=
literal|2
control|)
block|{
if|if
condition|(
name|i
operator|+
literal|1
operator|>
name|parts
operator|.
name|size
argument_list|()
condition|)
block|{
name|queries
operator|.
name|put
argument_list|(
name|parts
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|queries
operator|.
name|put
argument_list|(
name|parts
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|uriDecode
argument_list|(
name|parts
operator|.
name|get
argument_list|(
name|i
operator|+
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|queries
return|;
block|}
specifier|private
name|String
name|getRest
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|path
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PATH_INFO
argument_list|)
decl_stmt|;
name|String
name|basePath
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|BASE_PATH
argument_list|)
decl_stmt|;
if|if
condition|(
name|basePath
operator|==
literal|null
condition|)
block|{
name|basePath
operator|=
literal|"/"
expr_stmt|;
block|}
return|return
name|StringUtils
operator|.
name|diff
argument_list|(
name|path
argument_list|,
name|basePath
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getOperationName
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|rest
init|=
name|getRest
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|String
name|opName
init|=
name|StringUtils
operator|.
name|getFirstNotEmpty
argument_list|(
name|rest
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|opName
operator|.
name|indexOf
argument_list|(
literal|"?"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|opName
operator|=
name|opName
operator|.
name|split
argument_list|(
literal|"\\?"
argument_list|)
index|[
literal|0
index|]
expr_stmt|;
block|}
return|return
name|opName
return|;
block|}
block|}
end_class

end_unit

