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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|jwt
package|;
end_package

begin_class
specifier|public
class|class
name|JoseJwtProducerConsumer
block|{
specifier|private
name|JoseJwtProducer
name|producer
init|=
operator|new
name|JoseJwtProducer
argument_list|()
decl_stmt|;
specifier|private
name|JoseJwtConsumer
name|consumer
init|=
operator|new
name|JoseJwtConsumer
argument_list|()
decl_stmt|;
specifier|public
name|String
name|processJwt
parameter_list|(
name|JwtToken
name|jwt
parameter_list|)
block|{
return|return
name|producer
operator|.
name|processJwt
argument_list|(
name|jwt
argument_list|)
return|;
block|}
specifier|public
name|JwtToken
name|getJwtToken
parameter_list|(
name|String
name|wrappedJwtToken
parameter_list|)
block|{
return|return
name|consumer
operator|.
name|getJwtToken
argument_list|(
name|wrappedJwtToken
argument_list|)
return|;
block|}
specifier|public
name|void
name|setProducer
parameter_list|(
name|JoseJwtProducer
name|producer
parameter_list|)
block|{
name|this
operator|.
name|producer
operator|=
name|producer
expr_stmt|;
block|}
specifier|public
name|void
name|setConsumer
parameter_list|(
name|JoseJwtConsumer
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
block|}
end_class

end_unit
