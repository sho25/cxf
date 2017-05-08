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
name|cdi
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|Valid
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|constraints
operator|.
name|NotNull
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
name|GET
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

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Feed
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|parser
operator|.
name|stax
operator|.
name|FOMEntry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|parser
operator|.
name|stax
operator|.
name|FOMFeed
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
name|systests
operator|.
name|cdi
operator|.
name|base
operator|.
name|Book
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
name|systests
operator|.
name|cdi
operator|.
name|base
operator|.
name|BookStoreService
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/bookstore/"
argument_list|)
specifier|public
class|class
name|BookStoreFeed
block|{
specifier|private
specifier|final
name|BookStoreService
name|service
decl_stmt|;
specifier|public
name|BookStoreFeed
parameter_list|(
name|BookStoreService
name|service
parameter_list|)
block|{
name|this
operator|.
name|service
operator|=
name|service
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/feed"
argument_list|)
annotation|@
name|NotNull
annotation|@
name|Valid
annotation|@
name|Produces
argument_list|(
literal|"application/atom+xml"
argument_list|)
specifier|public
name|Feed
name|getBooks
parameter_list|()
block|{
specifier|final
name|FOMFeed
name|feed
init|=
operator|new
name|FOMFeed
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Book
name|book
range|:
name|service
operator|.
name|all
argument_list|()
control|)
block|{
specifier|final
name|FOMEntry
name|entry
init|=
operator|new
name|FOMEntry
argument_list|()
decl_stmt|;
name|entry
operator|.
name|addLink
argument_list|(
literal|"/bookstore/books/"
operator|+
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|feed
operator|.
name|addEntry
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
return|return
name|feed
return|;
block|}
block|}
end_class

end_unit

