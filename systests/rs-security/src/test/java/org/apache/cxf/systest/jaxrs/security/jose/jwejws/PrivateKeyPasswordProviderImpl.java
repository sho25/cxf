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
name|jaxrs
operator|.
name|security
operator|.
name|jose
operator|.
name|jwejws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|rt
operator|.
name|security
operator|.
name|rs
operator|.
name|PrivateKeyPasswordProvider
import|;
end_import

begin_class
specifier|public
class|class
name|PrivateKeyPasswordProviderImpl
implements|implements
name|PrivateKeyPasswordProvider
block|{
specifier|private
name|String
name|password
init|=
literal|"password"
decl_stmt|;
specifier|public
name|PrivateKeyPasswordProviderImpl
parameter_list|()
block|{      }
specifier|public
name|PrivateKeyPasswordProviderImpl
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|char
index|[]
name|getPassword
parameter_list|(
name|Properties
name|storeProperties
parameter_list|)
block|{
return|return
name|password
operator|.
name|toCharArray
argument_list|()
return|;
block|}
block|}
end_class

end_unit

