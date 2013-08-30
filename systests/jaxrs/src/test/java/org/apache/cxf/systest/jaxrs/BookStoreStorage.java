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
name|HashMap
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
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PreDestroy
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
name|Path
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/bookstorestorage/"
argument_list|)
specifier|public
specifier|abstract
class|class
name|BookStoreStorage
implements|implements
name|LifecycleInterface
block|{
specifier|protected
name|Map
argument_list|<
name|Long
argument_list|,
name|Book
argument_list|>
name|books
init|=
operator|new
name|HashMap
argument_list|<
name|Long
argument_list|,
name|Book
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|long
name|bookId
init|=
literal|123
decl_stmt|;
specifier|protected
name|int
name|postConstructCalls
decl_stmt|;
specifier|protected
name|int
name|preDestroyCalls
decl_stmt|;
annotation|@
name|PostConstruct
specifier|public
name|void
name|postConstruct
parameter_list|()
block|{
if|if
condition|(
name|postConstructCalls
operator|++
operator|==
literal|1
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
block|}
annotation|@
name|PreDestroy
specifier|public
name|void
name|preDestroy
parameter_list|()
block|{
if|if
condition|(
name|preDestroyCalls
operator|++
operator|==
literal|1
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
block|}
specifier|protected
name|void
name|checkPostConstruct
parameter_list|()
block|{
if|if
condition|(
name|postConstructCalls
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

