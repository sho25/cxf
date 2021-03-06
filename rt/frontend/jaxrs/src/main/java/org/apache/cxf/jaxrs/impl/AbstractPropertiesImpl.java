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
name|jaxrs
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|jaxrs
operator|.
name|impl
operator|.
name|PropertyHolderFactory
operator|.
name|PropertyHolder
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractPropertiesImpl
block|{
specifier|protected
name|Message
name|m
decl_stmt|;
specifier|private
name|PropertyHolder
name|holder
decl_stmt|;
specifier|public
name|AbstractPropertiesImpl
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|holder
operator|=
name|PropertyHolderFactory
operator|.
name|getPropertyHolder
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|this
operator|.
name|m
operator|=
name|message
expr_stmt|;
block|}
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|holder
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|void
name|removeProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|holder
operator|.
name|removeProperty
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|holder
operator|.
name|setProperty
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getPropertyNames
parameter_list|()
block|{
return|return
name|holder
operator|.
name|getPropertyNames
argument_list|()
return|;
block|}
specifier|public
name|Message
name|getMessage
parameter_list|()
block|{
return|return
name|m
return|;
block|}
block|}
end_class

end_unit

