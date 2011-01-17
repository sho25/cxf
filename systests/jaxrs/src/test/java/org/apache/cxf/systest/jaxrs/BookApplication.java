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

begin_class
annotation|@
name|ApplicationPath
argument_list|(
literal|"/thebooks"
argument_list|)
specifier|public
class|class
name|BookApplication
extends|extends
name|Application
block|{
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
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
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
name|BookStorePerRequest
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|jaxws
operator|.
name|BookStoreJaxrsJaxws
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|RuntimeExceptionMapper
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
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|classes
operator|.
name|add
argument_list|(
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
argument_list|()
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
return|return
name|classes
return|;
block|}
block|}
end_class

end_unit

