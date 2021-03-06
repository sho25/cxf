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
name|service
operator|.
name|model
package|;
end_package

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
name|List
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

begin_class
specifier|public
class|class
name|DescriptionInfo
extends|extends
name|AbstractPropertiesHolder
implements|implements
name|NamedItem
block|{
name|QName
name|name
decl_stmt|;
name|String
name|uri
decl_stmt|;
name|List
argument_list|<
name|AbstractDescriptionElement
argument_list|>
name|described
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|DescriptionInfo
parameter_list|()
block|{     }
specifier|public
name|void
name|setName
parameter_list|(
name|QName
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setBaseURI
parameter_list|(
name|String
name|u
parameter_list|)
block|{
name|uri
operator|=
name|u
expr_stmt|;
block|}
specifier|public
name|String
name|getBaseURI
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
specifier|public
name|List
argument_list|<
name|AbstractDescriptionElement
argument_list|>
name|getDescribed
parameter_list|()
block|{
return|return
name|described
return|;
block|}
block|}
end_class

end_unit

