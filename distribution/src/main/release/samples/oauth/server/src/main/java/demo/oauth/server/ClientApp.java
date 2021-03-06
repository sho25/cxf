begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|oauth
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_class
specifier|public
class|class
name|ClientApp
implements|implements
name|Serializable
block|{
specifier|private
name|String
name|clientName
init|=
literal|"OAuth 1.0a client"
decl_stmt|;
specifier|private
name|String
name|callbackURL
init|=
literal|"http://localhost:8080/app/callback"
decl_stmt|;
specifier|private
name|String
name|consumerKey
decl_stmt|;
specifier|private
name|String
name|error
decl_stmt|;
specifier|public
name|String
name|getClientName
parameter_list|()
block|{
return|return
name|clientName
return|;
block|}
specifier|public
name|void
name|setClientName
parameter_list|(
name|String
name|clientName
parameter_list|)
block|{
name|this
operator|.
name|clientName
operator|=
name|clientName
expr_stmt|;
block|}
specifier|public
name|String
name|getCallbackURL
parameter_list|()
block|{
return|return
name|callbackURL
return|;
block|}
specifier|public
name|void
name|setCallbackURL
parameter_list|(
name|String
name|callbackURL
parameter_list|)
block|{
name|this
operator|.
name|callbackURL
operator|=
name|callbackURL
expr_stmt|;
block|}
specifier|public
name|String
name|getError
parameter_list|()
block|{
return|return
name|error
return|;
block|}
specifier|public
name|void
name|setError
parameter_list|(
name|String
name|error
parameter_list|)
block|{
name|this
operator|.
name|error
operator|=
name|error
expr_stmt|;
block|}
specifier|public
name|String
name|getConsumerKey
parameter_list|()
block|{
return|return
name|consumerKey
return|;
block|}
specifier|public
name|void
name|setConsumerKey
parameter_list|(
name|String
name|consumerKey
parameter_list|)
block|{
name|this
operator|.
name|consumerKey
operator|=
name|consumerKey
expr_stmt|;
block|}
block|}
end_class

end_unit

