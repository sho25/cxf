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
name|ws
operator|.
name|security
operator|.
name|trust
operator|.
name|claims
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|Callback
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

begin_comment
comment|/**  * This Callback class provides a pluggable way of sending Claims to the STS. A CallbackHandler  * instance will be supplied with this class, which contains a reference to the current  * Message. The CallbackHandler implementation is required to set the claims Object to be  * sent in the request. This object can be either a DOM Element to be written out "as is", or else  * a org.apache.cxf.rt.security.claims.ClaimCollection Object which will be serialized in the  * request.  */
end_comment

begin_class
specifier|public
class|class
name|ClaimsCallback
implements|implements
name|Callback
block|{
specifier|private
name|Object
name|claims
decl_stmt|;
specifier|private
name|Message
name|currentMessage
decl_stmt|;
specifier|public
name|ClaimsCallback
parameter_list|()
block|{
comment|//
block|}
specifier|public
name|ClaimsCallback
parameter_list|(
name|Message
name|currentMessage
parameter_list|)
block|{
name|this
operator|.
name|currentMessage
operator|=
name|currentMessage
expr_stmt|;
block|}
specifier|public
name|void
name|setClaims
parameter_list|(
name|Object
name|claims
parameter_list|)
block|{
name|this
operator|.
name|claims
operator|=
name|claims
expr_stmt|;
block|}
specifier|public
name|Object
name|getClaims
parameter_list|()
block|{
return|return
name|claims
return|;
block|}
specifier|public
name|void
name|setCurrentMessage
parameter_list|(
name|Message
name|currentMessage
parameter_list|)
block|{
name|this
operator|.
name|currentMessage
operator|=
name|currentMessage
expr_stmt|;
block|}
specifier|public
name|Message
name|getCurrentMessage
parameter_list|()
block|{
return|return
name|currentMessage
return|;
block|}
block|}
end_class

end_unit

