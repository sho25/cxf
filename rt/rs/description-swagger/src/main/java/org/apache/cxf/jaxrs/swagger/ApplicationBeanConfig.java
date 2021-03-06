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
name|swagger
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Application
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|jaxrs
operator|.
name|config
operator|.
name|BeanConfig
import|;
end_import

begin_class
specifier|public
class|class
name|ApplicationBeanConfig
extends|extends
name|BeanConfig
block|{
specifier|private
name|Application
name|app
decl_stmt|;
specifier|public
name|ApplicationBeanConfig
parameter_list|(
name|Application
name|app
parameter_list|)
block|{
name|this
operator|.
name|app
operator|=
name|app
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|allClasses
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|allClasses
operator|.
name|addAll
argument_list|(
name|app
operator|.
name|getClasses
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|singleton
range|:
name|app
operator|.
name|getSingletons
argument_list|()
control|)
block|{
name|allClasses
operator|.
name|add
argument_list|(
name|singleton
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|allClasses
return|;
block|}
block|}
end_class

end_unit

