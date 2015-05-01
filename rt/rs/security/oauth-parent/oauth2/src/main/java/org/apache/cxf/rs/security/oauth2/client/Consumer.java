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
name|oauth2
operator|.
name|client
package|;
end_package

begin_class
specifier|public
class|class
name|Consumer
block|{
specifier|private
name|String
name|key
decl_stmt|;
specifier|private
name|String
name|secret
decl_stmt|;
specifier|private
name|String
name|description
decl_stmt|;
specifier|public
name|Consumer
parameter_list|()
block|{              }
specifier|public
name|Consumer
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|secret
parameter_list|)
block|{
name|this
operator|.
name|setKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|this
operator|.
name|setSecret
argument_list|(
name|secret
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
specifier|public
name|void
name|setKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
specifier|public
name|String
name|getSecret
parameter_list|()
block|{
return|return
name|secret
return|;
block|}
specifier|public
name|void
name|setSecret
parameter_list|(
name|String
name|secret
parameter_list|)
block|{
name|this
operator|.
name|secret
operator|=
name|secret
expr_stmt|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|key
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|instanceof
name|Consumer
operator|&&
name|key
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|Consumer
operator|)
name|o
operator|)
operator|.
name|key
argument_list|)
return|;
block|}
block|}
end_class

end_unit

