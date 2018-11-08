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
name|lang
operator|.
name|reflect
operator|.
name|Constructor
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
name|Field
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
name|util
operator|.
name|ArrayList
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
name|HashMap
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
name|StringTokenizer
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|ReflectionUtil
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
name|databinding
operator|.
name|DataBinding
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
name|databinding
operator|.
name|DataReader
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
name|helpers
operator|.
name|DOMUtils
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
name|helpers
operator|.
name|XPathUtils
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
name|FaultMode
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
name|staxutils
operator|.
name|W3CDOMStreamReader
import|;
end_import

begin_comment
comment|/**  * Takes a Fault and converts it to a local exception type if possible.  */
end_comment

begin_class
specifier|public
class|class
name|ClientFaultConverter
extends|extends
name|AbstractInDatabindingInterceptor
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DISABLE_FAULT_MAPPING
init|=
literal|"disable-fault-mapping"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Pattern
name|CAUSE_SUFFIX_SPLITTER
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|Message
operator|.
name|EXCEPTION_CAUSE_SUFFIX
argument_list|,
name|Pattern
operator|.
name|LITERAL
operator||
name|Pattern
operator|.
name|MULTILINE
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|ClientFaultConverter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|ClientFaultConverter
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
name|ClientFaultConverter
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|msg
parameter_list|)
block|{
name|Fault
name|fault
init|=
operator|(
name|Fault
operator|)
name|msg
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|fault
operator|.
name|getDetail
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|msg
argument_list|,
name|DISABLE_FAULT_MAPPING
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|processFaultDetail
argument_list|(
name|fault
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|setStackTrace
argument_list|(
name|fault
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
name|FaultMode
name|faultMode
init|=
name|FaultMode
operator|.
name|UNCHECKED_APPLICATION_FAULT
decl_stmt|;
comment|// Check if the raised exception is declared in the WSDL or by the JAX-RS resource
name|Method
name|m
init|=
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Method
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
name|Exception
name|e
init|=
name|msg
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|cl
range|:
name|m
operator|.
name|getExceptionTypes
argument_list|()
control|)
block|{
if|if
condition|(
name|cl
operator|.
name|isInstance
argument_list|(
name|e
argument_list|)
condition|)
block|{
name|faultMode
operator|=
name|FaultMode
operator|.
name|CHECKED_APPLICATION_FAULT
expr_stmt|;
break|break;
block|}
block|}
block|}
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|,
name|faultMode
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|processFaultDetail
parameter_list|(
name|Fault
name|fault
parameter_list|,
name|Message
name|msg
parameter_list|)
block|{
name|Element
name|exDetail
init|=
operator|(
name|Element
operator|)
name|DOMUtils
operator|.
name|getChild
argument_list|(
name|fault
operator|.
name|getDetail
argument_list|()
argument_list|,
name|Node
operator|.
name|ELEMENT_NODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|exDetail
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|QName
name|qname
init|=
operator|new
name|QName
argument_list|(
name|exDetail
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|exDetail
operator|.
name|getLocalName
argument_list|()
argument_list|)
decl_stmt|;
name|FaultInfo
name|faultWanted
init|=
literal|null
decl_stmt|;
name|MessagePartInfo
name|part
init|=
literal|null
decl_stmt|;
name|BindingOperationInfo
name|boi
init|=
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|boi
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|boi
operator|.
name|isUnwrapped
argument_list|()
condition|)
block|{
name|boi
operator|=
name|boi
operator|.
name|getWrappedOperation
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|FaultInfo
name|faultInfo
range|:
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getFaults
argument_list|()
control|)
block|{
for|for
control|(
name|MessagePartInfo
name|mpi
range|:
name|faultInfo
operator|.
name|getMessageParts
argument_list|()
control|)
block|{
if|if
condition|(
name|qname
operator|.
name|equals
argument_list|(
name|mpi
operator|.
name|getConcreteName
argument_list|()
argument_list|)
condition|)
block|{
name|faultWanted
operator|=
name|faultInfo
expr_stmt|;
name|part
operator|=
name|mpi
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|faultWanted
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
if|if
condition|(
name|faultWanted
operator|==
literal|null
condition|)
block|{
comment|//did not find it using the proper qualified names, we'll try again with just the localpart
for|for
control|(
name|FaultInfo
name|faultInfo
range|:
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getFaults
argument_list|()
control|)
block|{
for|for
control|(
name|MessagePartInfo
name|mpi
range|:
name|faultInfo
operator|.
name|getMessageParts
argument_list|()
control|)
block|{
if|if
condition|(
name|qname
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|mpi
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|faultWanted
operator|=
name|faultInfo
expr_stmt|;
name|part
operator|=
name|mpi
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|faultWanted
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
block|}
if|if
condition|(
name|faultWanted
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Service
name|s
init|=
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|DataBinding
name|dataBinding
init|=
name|s
operator|.
name|getDataBinding
argument_list|()
decl_stmt|;
name|Object
name|e
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isDOMSupported
argument_list|(
name|dataBinding
argument_list|)
condition|)
block|{
name|DataReader
argument_list|<
name|Node
argument_list|>
name|reader
init|=
name|this
operator|.
name|getNodeDataReader
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|reader
operator|.
name|setProperty
argument_list|(
name|DataReader
operator|.
name|FAULT
argument_list|,
name|fault
argument_list|)
expr_stmt|;
name|e
operator|=
name|reader
operator|.
name|read
argument_list|(
name|part
argument_list|,
name|exDetail
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|DataReader
argument_list|<
name|XMLStreamReader
argument_list|>
name|reader
init|=
name|this
operator|.
name|getDataReader
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xsr
init|=
operator|new
name|W3CDOMStreamReader
argument_list|(
name|exDetail
argument_list|)
decl_stmt|;
try|try
block|{
name|xsr
operator|.
name|nextTag
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e1
argument_list|)
throw|;
block|}
name|reader
operator|.
name|setProperty
argument_list|(
name|DataReader
operator|.
name|FAULT
argument_list|,
name|fault
argument_list|)
expr_stmt|;
name|e
operator|=
name|reader
operator|.
name|read
argument_list|(
name|part
argument_list|,
name|xsr
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
operator|(
name|e
operator|instanceof
name|Exception
operator|)
condition|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|exClass
init|=
name|faultWanted
operator|.
name|getProperty
argument_list|(
name|Class
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Class
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|exClass
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
name|Constructor
argument_list|<
name|?
argument_list|>
name|constructor
init|=
name|exClass
operator|.
name|getConstructor
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|e
operator|=
name|constructor
operator|.
name|newInstance
argument_list|(
name|fault
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|Constructor
argument_list|<
name|?
argument_list|>
name|constructor
init|=
name|getConstructor
argument_list|(
name|exClass
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|e
operator|=
name|constructor
operator|.
name|newInstance
argument_list|(
name|fault
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e1
parameter_list|)
block|{
comment|//Use reflection to convert fault bean to exception
name|e
operator|=
name|convertFaultBean
argument_list|(
name|exClass
argument_list|,
name|e
argument_list|,
name|fault
argument_list|)
expr_stmt|;
block|}
block|}
name|msg
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e1
parameter_list|)
block|{
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|INFO
argument_list|,
literal|"EXCEPTION_WHILE_CREATING_EXCEPTION"
argument_list|,
name|e1
argument_list|,
name|e1
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|fault
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Field
name|f
decl_stmt|;
try|try
block|{
name|f
operator|=
name|Throwable
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"detailMessage"
argument_list|)
expr_stmt|;
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|f
operator|.
name|set
argument_list|(
name|e
argument_list|,
name|fault
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e1
parameter_list|)
block|{
comment|//ignore
block|}
block|}
name|msg
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Constructor
argument_list|<
name|?
argument_list|>
name|getConstructor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|faultClass
parameter_list|,
name|Object
name|e
parameter_list|)
throws|throws
name|NoSuchMethodException
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|beanClass
init|=
name|e
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Constructor
argument_list|<
name|?
argument_list|>
index|[]
name|cons
init|=
name|faultClass
operator|.
name|getConstructors
argument_list|()
decl_stmt|;
for|for
control|(
name|Constructor
argument_list|<
name|?
argument_list|>
name|c
range|:
name|cons
control|)
block|{
if|if
condition|(
name|c
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|2
operator|&&
name|String
operator|.
name|class
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
argument_list|)
operator|&&
name|c
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|1
index|]
operator|.
name|isInstance
argument_list|(
name|e
argument_list|)
condition|)
block|{
return|return
name|c
return|;
block|}
block|}
try|try
block|{
return|return
name|faultClass
operator|.
name|getConstructor
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|beanClass
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|ex
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|getPrimitiveClass
argument_list|(
name|beanClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|cls
operator|!=
literal|null
condition|)
block|{
return|return
name|faultClass
operator|.
name|getConstructor
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|cls
argument_list|)
return|;
block|}
throw|throw
name|ex
throw|;
block|}
block|}
specifier|private
name|boolean
name|isDOMSupported
parameter_list|(
name|DataBinding
name|db
parameter_list|)
block|{
name|boolean
name|supportsDOM
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|db
operator|.
name|getSupportedReaderFormats
argument_list|()
control|)
block|{
if|if
condition|(
name|c
operator|.
name|equals
argument_list|(
name|Node
operator|.
name|class
argument_list|)
condition|)
block|{
name|supportsDOM
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|supportsDOM
return|;
block|}
specifier|private
name|void
name|setStackTrace
parameter_list|(
name|Fault
name|fault
parameter_list|,
name|Message
name|msg
parameter_list|)
block|{
name|Throwable
name|cause
init|=
literal|null
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|XPathUtils
name|xu
init|=
operator|new
name|XPathUtils
argument_list|(
name|ns
argument_list|)
decl_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"s"
argument_list|,
name|Fault
operator|.
name|STACKTRACE_NAMESPACE
argument_list|)
expr_stmt|;
name|String
name|ss
init|=
operator|(
name|String
operator|)
name|xu
operator|.
name|getValue
argument_list|(
literal|"//s:"
operator|+
name|Fault
operator|.
name|STACKTRACE
operator|+
literal|"/text()"
argument_list|,
name|fault
operator|.
name|getDetail
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|StackTraceElement
argument_list|>
name|stackTraceList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|ss
argument_list|)
condition|)
block|{
name|Iterator
argument_list|<
name|String
argument_list|>
name|linesIterator
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|CAUSE_SUFFIX_SPLITTER
operator|.
name|split
argument_list|(
name|ss
argument_list|)
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|linesIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|oneLine
init|=
name|linesIterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|oneLine
operator|.
name|startsWith
argument_list|(
literal|"Caused by:"
argument_list|)
condition|)
block|{
name|cause
operator|=
name|getCause
argument_list|(
name|linesIterator
argument_list|,
name|oneLine
argument_list|)
expr_stmt|;
break|break;
block|}
name|stackTraceList
operator|.
name|add
argument_list|(
name|parseStackTrackLine
argument_list|(
name|oneLine
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|stackTraceList
operator|.
name|isEmpty
argument_list|()
operator|||
name|cause
operator|!=
literal|null
condition|)
block|{
name|Exception
name|e
init|=
name|msg
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|stackTraceList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|StackTraceElement
index|[]
name|stackTraceElement
init|=
operator|new
name|StackTraceElement
index|[
name|stackTraceList
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|e
operator|.
name|setStackTrace
argument_list|(
name|stackTraceList
operator|.
name|toArray
argument_list|(
name|stackTraceElement
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cause
operator|!=
literal|null
operator|&&
name|cause
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
operator|&&
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|e
operator|.
name|setStackTrace
argument_list|(
name|cause
operator|.
name|getStackTrace
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cause
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|e
operator|.
name|initCause
argument_list|(
name|cause
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|cause
operator|!=
literal|null
condition|)
block|{
name|e
operator|.
name|initCause
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// recursively parse the causes and instantiate corresponding throwables
specifier|private
name|Throwable
name|getCause
parameter_list|(
name|Iterator
argument_list|<
name|String
argument_list|>
name|linesIterator
parameter_list|,
name|String
name|firstLine
parameter_list|)
block|{
comment|// The actual exception class of the cause might be unavailable at the
comment|// client -> use a standard throwable to represent the cause.
name|firstLine
operator|=
name|firstLine
operator|.
name|substring
argument_list|(
name|firstLine
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
name|Throwable
name|res
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|firstLine
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|cn
init|=
name|firstLine
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|firstLine
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|cn
operator|.
name|startsWith
argument_list|(
literal|"java.lang"
argument_list|)
condition|)
block|{
try|try
block|{
name|res
operator|=
operator|(
name|Throwable
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|cn
argument_list|)
operator|.
name|getConstructor
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|newInstance
argument_list|(
name|firstLine
operator|.
name|substring
argument_list|(
name|firstLine
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
operator|+
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore, use the default
block|}
block|}
block|}
if|if
condition|(
name|res
operator|==
literal|null
condition|)
block|{
name|res
operator|=
operator|new
name|Throwable
argument_list|(
name|firstLine
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|StackTraceElement
argument_list|>
name|stackTraceList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|linesIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|oneLine
init|=
name|linesIterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|oneLine
operator|.
name|startsWith
argument_list|(
literal|"Caused by:"
argument_list|)
condition|)
block|{
name|Throwable
name|nestedCause
init|=
name|getCause
argument_list|(
name|linesIterator
argument_list|,
name|oneLine
argument_list|)
decl_stmt|;
name|res
operator|.
name|initCause
argument_list|(
name|nestedCause
argument_list|)
expr_stmt|;
break|break;
block|}
name|stackTraceList
operator|.
name|add
argument_list|(
name|parseStackTrackLine
argument_list|(
name|oneLine
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|StackTraceElement
index|[]
name|stackTraceElement
init|=
operator|new
name|StackTraceElement
index|[
name|stackTraceList
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|res
operator|.
name|setStackTrace
argument_list|(
name|stackTraceList
operator|.
name|toArray
argument_list|(
name|stackTraceElement
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|res
return|;
block|}
specifier|private
specifier|static
name|StackTraceElement
name|parseStackTrackLine
parameter_list|(
name|String
name|oneLine
parameter_list|)
block|{
name|StringTokenizer
name|stInner
init|=
operator|new
name|StringTokenizer
argument_list|(
name|oneLine
argument_list|,
literal|"!"
argument_list|)
decl_stmt|;
return|return
operator|new
name|StackTraceElement
argument_list|(
name|stInner
operator|.
name|nextToken
argument_list|()
argument_list|,
name|stInner
operator|.
name|nextToken
argument_list|()
argument_list|,
name|stInner
operator|.
name|nextToken
argument_list|()
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|stInner
operator|.
name|nextToken
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|getPrimitiveClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
return|return
name|cls
return|;
block|}
try|try
block|{
name|Field
name|field
init|=
name|cls
operator|.
name|getField
argument_list|(
literal|"TYPE"
argument_list|)
decl_stmt|;
name|Object
name|obj
init|=
name|cls
decl_stmt|;
name|Object
name|type
init|=
name|field
operator|.
name|get
argument_list|(
name|obj
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|instanceof
name|Class
condition|)
block|{
return|return
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|type
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// do nothing
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|Exception
name|convertFaultBean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|exClass
parameter_list|,
name|Object
name|faultBean
parameter_list|,
name|Fault
name|fault
parameter_list|)
throws|throws
name|Exception
block|{
name|Constructor
argument_list|<
name|?
argument_list|>
name|constructor
init|=
name|exClass
operator|.
name|getConstructor
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exception
name|e
init|=
operator|(
name|Exception
operator|)
name|constructor
operator|.
name|newInstance
argument_list|(
name|fault
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
comment|//Copy fault bean fields to exception
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|obj
init|=
name|exClass
init|;
operator|!
name|obj
operator|.
name|equals
argument_list|(
name|Object
operator|.
name|class
argument_list|)
condition|;
name|obj
operator|=
name|obj
operator|.
name|getSuperclass
argument_list|()
control|)
block|{
name|Field
index|[]
name|fields
init|=
name|obj
operator|.
name|getDeclaredFields
argument_list|()
decl_stmt|;
for|for
control|(
name|Field
name|f
range|:
name|fields
control|)
block|{
try|try
block|{
name|Field
name|beanField
init|=
name|faultBean
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|beanField
argument_list|)
expr_stmt|;
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|f
operator|.
name|set
argument_list|(
name|e
argument_list|,
name|beanField
operator|.
name|get
argument_list|(
name|faultBean
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchFieldException
name|e1
parameter_list|)
block|{
comment|//do nothing
block|}
block|}
block|}
comment|//also use/try public getter/setter methods
name|Method
index|[]
name|meth
init|=
name|faultBean
operator|.
name|getClass
argument_list|()
operator|.
name|getMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|m
range|:
name|meth
control|)
block|{
if|if
condition|(
name|m
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|0
operator|&&
operator|(
name|m
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"get"
argument_list|)
operator|||
name|m
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"is"
argument_list|)
operator|)
condition|)
block|{
try|try
block|{
name|String
name|name
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"get"
argument_list|)
condition|)
block|{
name|name
operator|=
literal|"set"
operator|+
name|m
operator|.
name|getName
argument_list|()
operator|.
name|substring
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
literal|"set"
operator|+
name|m
operator|.
name|getName
argument_list|()
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
name|Method
name|m2
init|=
name|exClass
operator|.
name|getMethod
argument_list|(
name|name
argument_list|,
name|m
operator|.
name|getReturnType
argument_list|()
argument_list|)
decl_stmt|;
name|m2
operator|.
name|invoke
argument_list|(
name|e
argument_list|,
name|m
operator|.
name|invoke
argument_list|(
name|faultBean
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e1
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
return|return
name|e
return|;
block|}
block|}
end_class

end_unit

