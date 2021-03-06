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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

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
name|ws
operator|.
name|rs
operator|.
name|ApplicationPath
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Context
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
name|UriInfo
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
name|openapi
operator|.
name|OpenApiFeature
import|;
end_import

begin_class
annotation|@
name|ApplicationPath
argument_list|(
literal|"/"
argument_list|)
annotation|@
name|GlobalNameBinding
specifier|public
class|class
name|BookApplicationNonSpring
extends|extends
name|Application
block|{
specifier|private
name|String
name|defaultName
decl_stmt|;
specifier|private
name|long
name|defaultId
decl_stmt|;
annotation|@
name|Context
specifier|private
name|UriInfo
name|uriInfo
decl_stmt|;
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
name|getClasses
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|classes
operator|.
name|add
argument_list|(
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
name|BookStore
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|classes
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Object
argument_list|>
name|getSingletons
parameter_list|()
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|classes
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
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
name|BookStore
name|store
init|=
operator|new
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
name|BookStore
argument_list|(
name|uriInfo
argument_list|)
decl_stmt|;
name|store
operator|.
name|setDefaultNameAndId
argument_list|(
name|defaultName
argument_list|,
name|defaultId
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|store
argument_list|)
expr_stmt|;
name|BookExceptionMapper
name|mapper
init|=
operator|new
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
name|BookExceptionMapper
argument_list|()
decl_stmt|;
name|mapper
operator|.
name|setToHandle
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|mapper
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
operator|new
name|OpenApiFeature
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|classes
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|Collections
operator|.
expr|<
name|String
operator|,
name|Object
operator|>
name|singletonMap
argument_list|(
literal|"book"
argument_list|,
literal|"cxf"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setDefaultName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|defaultName
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|void
name|setDefaultId
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|ids
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|id
range|:
name|ids
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|defaultId
operator|=
name|Long
operator|.
name|valueOf
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

