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
name|ServerRequest
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaServerConduit
implements|implements
name|Conduit
block|{
specifier|private
name|EndpointInfo
name|endpointInfo
decl_stmt|;
specifier|private
name|EndpointReferenceType
name|target
decl_stmt|;
specifier|private
name|ORB
name|orb
decl_stmt|;
specifier|private
name|CorbaTypeMap
name|typeMap
decl_stmt|;
specifier|private
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|Object
name|targetObject
decl_stmt|;
specifier|public
name|CorbaServerConduit
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|EndpointReferenceType
name|ref
parameter_list|,
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|Object
name|targetObj
parameter_list|,
name|ORB
name|o
parameter_list|,
name|OrbConfig
name|config
parameter_list|,
name|CorbaTypeMap
name|map
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
if|if
condition|(
name|o
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
name|config
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|orb
operator|=
name|o
expr_stmt|;
block|}
name|typeMap
operator|=
name|map
expr_stmt|;
name|targetObject
operator|=
name|targetObj
expr_stmt|;
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
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|instanceof
name|CorbaMessage
condition|)
block|{
operator|(
operator|(
name|CorbaMessage
operator|)
name|message
operator|)
operator|.
name|setCorbaTypeMap
argument_list|(
name|typeMap
argument_list|)
expr_stmt|;
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
operator|instanceof
name|CorbaMessage
condition|)
block|{
name|buildRequestResult
argument_list|(
operator|(
name|CorbaMessage
operator|)
name|message
argument_list|)
expr_stmt|;
block|}
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
block|{     }
specifier|public
name|void
name|setMessageObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
block|{     }
specifier|public
name|MessageObserver
name|getMessageObserver
parameter_list|()
block|{
return|return
literal|null
return|;
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
name|buildRequestResult
parameter_list|(
name|CorbaMessage
name|msg
parameter_list|)
block|{
name|Exchange
name|exg
init|=
name|msg
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|ServerRequest
name|request
init|=
name|exg
operator|.
name|get
argument_list|(
name|ServerRequest
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|exg
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
name|CorbaMessage
name|inMsg
init|=
operator|(
name|CorbaMessage
operator|)
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
name|NVList
name|list
init|=
name|inMsg
operator|.
name|getList
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|.
name|getStreamableException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Any
name|exAny
init|=
name|CorbaAnyHelper
operator|.
name|createAny
argument_list|(
name|orb
argument_list|)
decl_stmt|;
name|CorbaStreamable
name|exception
init|=
name|msg
operator|.
name|getStreamableException
argument_list|()
decl_stmt|;
name|exAny
operator|.
name|insert_Streamable
argument_list|(
name|exception
argument_list|)
expr_stmt|;
name|request
operator|.
name|set_exception
argument_list|(
name|exAny
argument_list|)
expr_stmt|;
if|if
condition|(
name|msg
operator|.
name|getExchange
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|setOutFaultMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|CorbaStreamable
index|[]
name|arguments
init|=
name|msg
operator|.
name|getStreamableArguments
argument_list|()
decl_stmt|;
if|if
condition|(
name|arguments
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
name|arguments
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
operator|.
name|flags
argument_list|()
operator|!=
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ARG_IN
operator|.
name|value
condition|)
block|{
name|arguments
index|[
name|i
index|]
operator|.
name|getObject
argument_list|()
operator|.
name|setIntoAny
argument_list|(
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
operator|.
name|value
argument_list|()
argument_list|,
name|arguments
index|[
name|i
index|]
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|CorbaStreamable
name|resultValue
init|=
name|msg
operator|.
name|getStreamableReturn
argument_list|()
decl_stmt|;
if|if
condition|(
name|resultValue
operator|!=
literal|null
condition|)
block|{
name|Any
name|resultAny
init|=
name|CorbaAnyHelper
operator|.
name|createAny
argument_list|(
name|orb
argument_list|)
decl_stmt|;
name|resultValue
operator|.
name|getObject
argument_list|()
operator|.
name|setIntoAny
argument_list|(
name|resultAny
argument_list|,
name|resultValue
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|request
operator|.
name|set_result
argument_list|(
name|resultAny
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Exception during buildRequestResult"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
class|class
name|CorbaOutputStream
extends|extends
name|CachedOutputStream
block|{
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
block|{         }
specifier|public
name|void
name|onWrite
parameter_list|()
throws|throws
name|IOException
block|{          }
block|}
block|}
end_class

end_unit

