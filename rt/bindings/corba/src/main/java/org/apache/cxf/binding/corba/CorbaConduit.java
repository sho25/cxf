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
name|binding
operator|.
name|corba
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|corba
operator|.
name|types
operator|.
name|CorbaHandlerUtils
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
name|binding
operator|.
name|corba
operator|.
name|types
operator|.
name|CorbaObjectHandler
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
name|binding
operator|.
name|corba
operator|.
name|utils
operator|.
name|ContextUtils
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
name|binding
operator|.
name|corba
operator|.
name|utils
operator|.
name|CorbaAnyHelper
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
name|binding
operator|.
name|corba
operator|.
name|utils
operator|.
name|CorbaBindingHelper
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
name|binding
operator|.
name|corba
operator|.
name|utils
operator|.
name|CorbaUtils
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
name|binding
operator|.
name|corba
operator|.
name|utils
operator|.
name|OrbConfig
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
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|AddressType
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
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|CorbaConstants
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
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|OperationType
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
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|RaisesType
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
name|interceptor
operator|.
name|Fault
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
name|io
operator|.
name|CachedOutputStream
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
name|MessageImpl
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
name|EndpointInfo
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
name|ServiceInfo
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
name|transport
operator|.
name|Conduit
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
name|transport
operator|.
name|MessageObserver
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
name|ws
operator|.
name|addressing
operator|.
name|AttributedURIType
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|Any
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ContextList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ExceptionList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|NVList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|NamedValue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ORB
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|SystemException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|TypeCode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|UnknownUserException
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaConduit
implements|implements
name|Conduit
block|{
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
name|CorbaConduit
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|EndpointInfo
name|endpointInfo
decl_stmt|;
specifier|private
name|EndpointReferenceType
name|target
decl_stmt|;
specifier|private
name|MessageObserver
name|incomingObserver
decl_stmt|;
specifier|private
name|ORB
name|orb
decl_stmt|;
specifier|private
name|OrbConfig
name|orbConfig
decl_stmt|;
specifier|private
name|CorbaTypeMap
name|typeMap
decl_stmt|;
specifier|public
name|CorbaConduit
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|EndpointReferenceType
name|ref
parameter_list|,
name|OrbConfig
name|config
parameter_list|)
block|{
name|endpointInfo
operator|=
name|ei
expr_stmt|;
name|target
operator|=
name|getTargetReference
argument_list|(
name|ref
argument_list|)
expr_stmt|;
name|orbConfig
operator|=
name|config
expr_stmt|;
name|typeMap
operator|=
name|TypeMapCache
operator|.
name|get
argument_list|(
name|ei
operator|.
name|getService
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|OrbConfig
name|getOrbConfig
parameter_list|()
block|{
return|return
name|orbConfig
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|prepareOrb
parameter_list|()
block|{
if|if
condition|(
name|orb
operator|==
literal|null
condition|)
block|{
name|orb
operator|=
name|CorbaBindingHelper
operator|.
name|getDefaultORB
argument_list|(
name|orbConfig
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|prepare
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|prepareOrb
argument_list|()
expr_stmt|;
name|String
name|address
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
name|address
operator|=
name|target
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|address
operator|==
literal|null
condition|)
block|{
name|AddressType
name|ad
init|=
name|endpointInfo
operator|.
name|getExtensor
argument_list|(
name|AddressType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ad
operator|!=
literal|null
condition|)
block|{
name|address
operator|=
name|ad
operator|.
name|getLocation
argument_list|()
expr_stmt|;
block|}
block|}
name|String
name|ref
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
name|ENDPOINT_ADDRESS
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
comment|// A non-null endpoint address from the message means that we want to invoke on a particular
comment|// object reference specified by the endpoint reference type.  If the reference is null, then
comment|// we want to invoke on the default location as specified in the WSDL.
name|address
operator|=
name|ref
expr_stmt|;
block|}
if|if
condition|(
name|address
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
name|SEVERE
argument_list|,
literal|"Unable to locate a valid CORBA address"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Unable to locate a valid CORBA address"
argument_list|)
throw|;
block|}
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|Object
name|targetObject
decl_stmt|;
name|targetObject
operator|=
name|CorbaUtils
operator|.
name|importObjectReference
argument_list|(
name|orb
argument_list|,
name|address
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|CorbaConstants
operator|.
name|ORB
argument_list|,
name|orb
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|CorbaConstants
operator|.
name|CORBA_ENDPOINT_OBJECT
argument_list|,
name|targetObject
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
operator|new
name|CorbaOutputStream
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|CorbaMessage
name|corbaMessage
init|=
operator|(
name|CorbaMessage
operator|)
name|message
decl_stmt|;
name|corbaMessage
operator|.
name|setCorbaTypeMap
argument_list|(
name|typeMap
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"Could not resolve target object"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|close
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|message
operator|.
name|get
argument_list|(
name|CorbaConstants
operator|.
name|CORBA_ENDPOINT_OBJECT
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|BindingOperationInfo
name|boi
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|OperationType
name|opType
init|=
name|boi
operator|.
name|getExtensor
argument_list|(
name|OperationType
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|buildRequest
argument_list|(
operator|(
name|CorbaMessage
operator|)
name|message
argument_list|,
name|opType
argument_list|)
expr_stmt|;
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"Could not build the corba request"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
specifier|protected
name|void
name|finalize
parameter_list|()
throws|throws
name|Throwable
block|{
name|super
operator|.
name|finalize
argument_list|()
expr_stmt|;
block|}
specifier|public
name|EndpointReferenceType
name|getTarget
parameter_list|()
block|{
return|return
name|target
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{              }
specifier|public
name|void
name|setMessageObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
block|{
name|incomingObserver
operator|=
name|observer
expr_stmt|;
block|}
specifier|public
specifier|final
name|EndpointReferenceType
name|getTargetReference
parameter_list|(
name|EndpointReferenceType
name|t
parameter_list|)
block|{
name|EndpointReferenceType
name|ref
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|t
condition|)
block|{
name|ref
operator|=
operator|new
name|EndpointReferenceType
argument_list|()
expr_stmt|;
name|AttributedURIType
name|address
init|=
operator|new
name|AttributedURIType
argument_list|()
decl_stmt|;
name|address
operator|.
name|setValue
argument_list|(
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ref
operator|=
name|t
expr_stmt|;
block|}
return|return
name|ref
return|;
block|}
specifier|public
specifier|final
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|endpointInfo
operator|.
name|getAddress
argument_list|()
return|;
block|}
specifier|public
name|void
name|buildRequest
parameter_list|(
name|CorbaMessage
name|message
parameter_list|,
name|OperationType
name|opType
parameter_list|)
throws|throws
name|Exception
block|{
name|ServiceInfo
name|service
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|ServiceInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|NVList
name|nvlist
init|=
name|getArguments
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|NamedValue
name|ret
init|=
name|getReturn
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|TypeCode
argument_list|,
name|RaisesType
argument_list|>
name|exceptions
init|=
name|getOperationExceptions
argument_list|(
name|opType
argument_list|,
name|typeMap
argument_list|)
decl_stmt|;
name|ExceptionList
name|exList
init|=
name|getExceptionList
argument_list|(
name|exceptions
argument_list|,
name|message
argument_list|,
name|opType
argument_list|)
decl_stmt|;
name|Request
name|request
init|=
name|getRequest
argument_list|(
name|message
argument_list|,
name|opType
operator|.
name|getName
argument_list|()
argument_list|,
name|nvlist
argument_list|,
name|ret
argument_list|,
name|exList
argument_list|)
decl_stmt|;
if|if
condition|(
name|request
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Couldn't build the corba request"
argument_list|)
throw|;
block|}
try|try
block|{
name|request
operator|.
name|invoke
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SystemException
name|ex
parameter_list|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setSystemException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
return|return;
block|}
name|Exception
name|ex
init|=
name|request
operator|.
name|env
argument_list|()
operator|.
name|exception
argument_list|()
decl_stmt|;
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ex
operator|instanceof
name|UnknownUserException
condition|)
block|{
name|UnknownUserException
name|userEx
init|=
operator|(
name|UnknownUserException
operator|)
name|ex
decl_stmt|;
name|Any
name|except
init|=
name|userEx
operator|.
name|except
decl_stmt|;
name|RaisesType
name|raises
init|=
name|exceptions
operator|.
name|get
argument_list|(
name|except
operator|.
name|type
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|raises
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Couldn't find the exception type code to unmarshall"
argument_list|)
throw|;
block|}
name|QName
name|elName
init|=
operator|new
name|QName
argument_list|(
literal|""
argument_list|,
name|raises
operator|.
name|getException
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
name|CorbaObjectHandler
name|handler
init|=
name|CorbaHandlerUtils
operator|.
name|initializeObjectHandler
argument_list|(
name|orb
argument_list|,
name|elName
argument_list|,
name|raises
operator|.
name|getException
argument_list|()
argument_list|,
name|typeMap
argument_list|,
name|service
argument_list|)
decl_stmt|;
name|CorbaStreamable
name|exStreamable
init|=
name|message
operator|.
name|createStreamableObject
argument_list|(
name|handler
argument_list|,
name|elName
argument_list|)
decl_stmt|;
name|exStreamable
operator|.
name|_read
argument_list|(
name|except
operator|.
name|create_input_stream
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setStreamableException
argument_list|(
name|exStreamable
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
operator|new
name|Fault
argument_list|(
name|userEx
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|NVList
name|getArguments
parameter_list|(
name|CorbaMessage
name|message
parameter_list|)
block|{
if|if
condition|(
name|orb
operator|==
literal|null
condition|)
block|{
name|prepareOrb
argument_list|()
expr_stmt|;
block|}
comment|// Build the list of DII arguments, returns, and exceptions
name|NVList
name|list
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|message
operator|.
name|getStreamableArguments
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|CorbaStreamable
index|[]
name|arguments
init|=
name|message
operator|.
name|getStreamableArguments
argument_list|()
decl_stmt|;
name|list
operator|=
name|orb
operator|.
name|create_list
argument_list|(
name|arguments
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|CorbaStreamable
name|argument
range|:
name|arguments
control|)
block|{
name|Any
name|value
init|=
name|CorbaAnyHelper
operator|.
name|createAny
argument_list|(
name|orb
argument_list|)
decl_stmt|;
name|argument
operator|.
name|getObject
argument_list|()
operator|.
name|setIntoAny
argument_list|(
name|value
argument_list|,
name|argument
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|list
operator|.
name|add_value
argument_list|(
name|argument
operator|.
name|getName
argument_list|()
argument_list|,
name|value
argument_list|,
name|argument
operator|.
name|getMode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|list
operator|=
name|orb
operator|.
name|create_list
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
specifier|public
name|NamedValue
name|getReturn
parameter_list|(
name|CorbaMessage
name|message
parameter_list|)
block|{
if|if
condition|(
name|orb
operator|==
literal|null
condition|)
block|{
name|prepareOrb
argument_list|()
expr_stmt|;
block|}
name|CorbaStreamable
name|retVal
init|=
name|message
operator|.
name|getStreamableReturn
argument_list|()
decl_stmt|;
name|NamedValue
name|ret
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|retVal
operator|!=
literal|null
condition|)
block|{
name|Any
name|returnAny
init|=
name|CorbaAnyHelper
operator|.
name|createAny
argument_list|(
name|orb
argument_list|)
decl_stmt|;
name|retVal
operator|.
name|getObject
argument_list|()
operator|.
name|setIntoAny
argument_list|(
name|returnAny
argument_list|,
name|retVal
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|ret
operator|=
name|orb
operator|.
name|create_named_value
argument_list|(
name|retVal
operator|.
name|getName
argument_list|()
argument_list|,
name|returnAny
argument_list|,
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ARG_OUT
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// TODO: REVISIT: for some reason,some ORBs do not like to
comment|// have a null NamedValue return value. Create this 'empty'
comment|// one if a void return type is used.
name|ret
operator|=
name|orb
operator|.
name|create_named_value
argument_list|(
literal|"return"
argument_list|,
name|orb
operator|.
name|create_any
argument_list|()
argument_list|,
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ARG_OUT
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
name|ExceptionList
name|getExceptionList
parameter_list|(
name|Map
argument_list|<
name|TypeCode
argument_list|,
name|RaisesType
argument_list|>
name|exceptions
parameter_list|,
name|CorbaMessage
name|message
parameter_list|,
name|OperationType
name|opType
parameter_list|)
block|{
if|if
condition|(
name|orb
operator|==
literal|null
condition|)
block|{
name|prepareOrb
argument_list|()
expr_stmt|;
block|}
comment|// Get the typecodes for the exceptions this operation can throw.
comment|// These are defined in the operation definition from WSDL.
name|ExceptionList
name|exList
init|=
name|orb
operator|.
name|create_exception_list
argument_list|()
decl_stmt|;
if|if
condition|(
name|exceptions
operator|!=
literal|null
condition|)
block|{
name|Object
index|[]
name|tcs
init|=
literal|null
decl_stmt|;
name|tcs
operator|=
name|exceptions
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|exceptions
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|exList
operator|.
name|add
argument_list|(
operator|(
name|TypeCode
operator|)
name|tcs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|exList
return|;
block|}
specifier|public
name|Request
name|getRequest
parameter_list|(
name|CorbaMessage
name|message
parameter_list|,
name|String
name|opName
parameter_list|,
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|NVList
name|nvlist
parameter_list|,
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|NamedValue
name|ret
parameter_list|,
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ExceptionList
name|exList
parameter_list|)
throws|throws
name|Exception
block|{
name|Request
name|request
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|orb
operator|==
literal|null
condition|)
block|{
name|prepareOrb
argument_list|()
expr_stmt|;
block|}
name|ContextList
name|ctxList
init|=
name|orb
operator|.
name|create_context_list
argument_list|()
decl_stmt|;
name|Context
name|ctx
init|=
literal|null
decl_stmt|;
try|try
block|{
name|ctx
operator|=
name|orb
operator|.
name|get_default_context
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore?
block|}
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|Object
name|targetObj
init|=
operator|(
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|Object
operator|)
name|message
operator|.
name|get
argument_list|(
name|CorbaConstants
operator|.
name|CORBA_ENDPOINT_OBJECT
argument_list|)
decl_stmt|;
if|if
condition|(
name|targetObj
operator|!=
literal|null
condition|)
block|{
name|request
operator|=
name|targetObj
operator|.
name|_create_request
argument_list|(
name|ctx
argument_list|,
name|opName
argument_list|,
name|nvlist
argument_list|,
name|ret
argument_list|,
name|exList
argument_list|,
name|ctxList
argument_list|)
expr_stmt|;
block|}
return|return
name|request
return|;
block|}
specifier|public
name|Map
argument_list|<
name|TypeCode
argument_list|,
name|RaisesType
argument_list|>
name|getOperationExceptions
parameter_list|(
name|OperationType
name|operation
parameter_list|,
name|CorbaTypeMap
name|map
parameter_list|)
block|{
if|if
condition|(
name|orb
operator|==
literal|null
condition|)
block|{
name|prepareOrb
argument_list|()
expr_stmt|;
block|}
name|Map
argument_list|<
name|TypeCode
argument_list|,
name|RaisesType
argument_list|>
name|exceptions
init|=
operator|new
name|HashMap
argument_list|<
name|TypeCode
argument_list|,
name|RaisesType
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|RaisesType
argument_list|>
name|exList
init|=
name|operation
operator|.
name|getRaises
argument_list|()
decl_stmt|;
if|if
condition|(
name|exList
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|exList
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|RaisesType
name|ex
init|=
name|exList
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|TypeCode
name|tc
init|=
name|CorbaUtils
operator|.
name|getTypeCode
argument_list|(
name|orb
argument_list|,
name|ex
operator|.
name|getException
argument_list|()
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|exceptions
operator|.
name|put
argument_list|(
name|tc
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|exceptions
return|;
block|}
specifier|private
class|class
name|CorbaOutputStream
extends|extends
name|CachedOutputStream
block|{
specifier|private
name|Message
name|message
decl_stmt|;
specifier|private
name|boolean
name|isOneWay
decl_stmt|;
name|CorbaOutputStream
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|message
operator|=
name|m
expr_stmt|;
block|}
comment|/**          * Perform any actions required on stream flush (freeze headers, reset          * output stream ... etc.)          */
specifier|public
name|void
name|doFlush
parameter_list|()
throws|throws
name|IOException
block|{
comment|// do nothing here
block|}
comment|/**          * Perform any actions required on stream closure (handle response etc.)          */
specifier|public
name|void
name|doClose
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|ContextUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
operator|&&
name|ContextUtils
operator|.
name|isOutbound
argument_list|(
name|message
argument_list|)
condition|)
block|{
try|try
block|{
name|isOneWay
operator|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|isOneWay
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|isOneWay
condition|)
block|{
name|handleResponse
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Connection failed with Exception : "
argument_list|,
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|ex
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
name|void
name|onWrite
parameter_list|()
throws|throws
name|IOException
block|{          }
specifier|public
name|void
name|handleResponse
parameter_list|()
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"incoming observer is "
operator|+
name|incomingObserver
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|CorbaMessage
name|corbaMsg
init|=
operator|(
name|CorbaMessage
operator|)
name|message
decl_stmt|;
name|MessageImpl
name|inMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|CorbaDestination
name|destination
init|=
operator|new
name|CorbaDestination
argument_list|(
name|endpointInfo
argument_list|,
name|orbConfig
argument_list|,
name|typeMap
argument_list|)
decl_stmt|;
name|inMessage
operator|.
name|setDestination
argument_list|(
name|destination
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|ORB
operator|.
name|class
argument_list|,
name|orb
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|CorbaMessage
name|inCorbaMsg
init|=
operator|new
name|CorbaMessage
argument_list|(
name|inMessage
argument_list|)
decl_stmt|;
name|inCorbaMsg
operator|.
name|setCorbaTypeMap
argument_list|(
name|typeMap
argument_list|)
expr_stmt|;
if|if
condition|(
name|corbaMsg
operator|.
name|getStreamableException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setInFaultMessage
argument_list|(
name|corbaMsg
argument_list|)
expr_stmt|;
name|inCorbaMsg
operator|.
name|setStreamableException
argument_list|(
name|corbaMsg
operator|.
name|getStreamableException
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|corbaMsg
operator|.
name|getSystemException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setInFaultMessage
argument_list|(
name|corbaMsg
argument_list|)
expr_stmt|;
name|inCorbaMsg
operator|.
name|setSystemException
argument_list|(
name|corbaMsg
operator|.
name|getSystemException
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"incoming observer is "
operator|+
name|incomingObserver
argument_list|)
expr_stmt|;
name|incomingObserver
operator|.
name|onMessage
argument_list|(
operator|(
name|Message
operator|)
name|inCorbaMsg
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|inCorbaMsg
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|MessageObserver
name|getMessageObserver
parameter_list|()
block|{
return|return
name|this
operator|.
name|incomingObserver
return|;
block|}
block|}
end_class

end_unit

