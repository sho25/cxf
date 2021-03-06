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
name|ext
operator|.
name|search
operator|.
name|jpa
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|metamodel
operator|.
name|ListAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|metamodel
operator|.
name|SingularAttribute
import|;
end_import

begin_class
annotation|@
name|javax
operator|.
name|persistence
operator|.
name|metamodel
operator|.
name|StaticMetamodel
argument_list|(
name|Book
operator|.
name|class
argument_list|)
comment|//CHECKSTYLE:OFF
specifier|public
specifier|final
class|class
name|Book_
block|{
specifier|private
name|Book_
parameter_list|()
block|{      }
specifier|public
specifier|static
specifier|volatile
name|SingularAttribute
argument_list|<
name|Book
argument_list|,
name|Integer
argument_list|>
name|id
decl_stmt|;
specifier|public
specifier|static
specifier|volatile
name|SingularAttribute
argument_list|<
name|Book
argument_list|,
name|String
argument_list|>
name|bookTitle
decl_stmt|;
specifier|public
specifier|static
specifier|volatile
name|SingularAttribute
argument_list|<
name|Book
argument_list|,
name|Library
argument_list|>
name|library
decl_stmt|;
specifier|public
specifier|static
specifier|volatile
name|SingularAttribute
argument_list|<
name|Book
argument_list|,
name|OwnerInfo
argument_list|>
name|ownerInfo
decl_stmt|;
specifier|public
specifier|static
specifier|volatile
name|SingularAttribute
argument_list|<
name|Book
argument_list|,
name|OwnerAddress
argument_list|>
name|address
decl_stmt|;
specifier|public
specifier|static
specifier|volatile
name|ListAttribute
argument_list|<
name|Book
argument_list|,
name|BookReview
argument_list|>
name|reviews
decl_stmt|;
specifier|public
specifier|static
specifier|volatile
name|ListAttribute
argument_list|<
name|Book
argument_list|,
name|String
argument_list|>
name|authors
decl_stmt|;
block|}
end_class

begin_comment
comment|//CHECKSTYLE:ON
end_comment

end_unit

