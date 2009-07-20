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
name|resources
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
name|POST
import|;
end_import

begin_class
specifier|public
class|class
name|CollectionsResource
block|{
annotation|@
name|GET
specifier|public
name|List
argument_list|<
name|Book
argument_list|>
name|getBooks
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|GET
specifier|public
name|Collection
argument_list|<
name|Book
argument_list|>
name|getBookCollection
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|GET
specifier|public
name|Set
argument_list|<
name|Book
argument_list|>
name|getBookSet
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|GET
specifier|public
name|List
argument_list|<
name|TagVO2
argument_list|>
name|getTags
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|GET
specifier|public
name|Book
index|[]
name|getBooksArray
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|POST
specifier|public
name|void
name|setBooks
parameter_list|(
name|List
argument_list|<
name|Book
argument_list|>
name|books
parameter_list|)
block|{     }
annotation|@
name|POST
specifier|public
name|void
name|setBooksArray
parameter_list|(
name|Book
index|[]
name|books
parameter_list|)
block|{     }
annotation|@
name|POST
specifier|public
name|void
name|setTags
parameter_list|(
name|List
argument_list|<
name|TagVO2
argument_list|>
name|tags
parameter_list|)
block|{     }
annotation|@
name|POST
specifier|public
name|void
name|setTagsArray
parameter_list|(
name|TagVO2
index|[]
name|tags
parameter_list|)
block|{     }
annotation|@
name|POST
specifier|public
name|void
name|setBookCollection
parameter_list|(
name|Collection
argument_list|<
name|Book
argument_list|>
name|books
parameter_list|)
block|{     }
annotation|@
name|POST
specifier|public
name|void
name|setBookSet
parameter_list|(
name|Set
argument_list|<
name|Book
argument_list|>
name|books
parameter_list|)
block|{     }
block|}
end_class

end_unit

