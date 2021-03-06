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
name|systest
operator|.
name|handlers
package|;
end_package

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
name|Set
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
name|ws
operator|.
name|handler
operator|.
name|MessageContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|soap
operator|.
name|SOAPHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|soap
operator|.
name|SOAPMessageContext
import|;
end_import

begin_class
specifier|public
class|class
name|MessageContextSecondHandler
implements|implements
name|SOAPHandler
argument_list|<
name|SOAPMessageContext
argument_list|>
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headerMap
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|boolean
name|handleMessage
parameter_list|(
name|SOAPMessageContext
name|context
parameter_list|)
block|{
name|boolean
name|isOutbound
init|=
operator|(
name|boolean
operator|)
name|context
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|MESSAGE_OUTBOUND_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|isOutbound
condition|)
block|{
name|headerMap
operator|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
operator|)
name|context
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|HTTP_REQUEST_HEADERS
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|handleFault
parameter_list|(
name|SOAPMessageContext
name|context
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|(
name|MessageContext
name|context
parameter_list|)
block|{     }
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|QName
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getHeaderMap
parameter_list|()
block|{
return|return
name|headerMap
return|;
block|}
block|}
end_class

end_unit

