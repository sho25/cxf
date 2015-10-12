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
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|persistence
operator|.
name|ElementCollection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Id
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|OneToOne
import|;
end_import

begin_class
annotation|@
name|Entity
specifier|public
class|class
name|BookReview
block|{
specifier|private
name|int
name|id
decl_stmt|;
specifier|private
name|Review
name|review
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|authors
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Book
name|book
decl_stmt|;
specifier|public
name|Review
name|getReview
parameter_list|()
block|{
return|return
name|review
return|;
block|}
specifier|public
name|void
name|setReview
parameter_list|(
name|Review
name|review
parameter_list|)
block|{
name|this
operator|.
name|review
operator|=
name|review
expr_stmt|;
block|}
annotation|@
name|OneToOne
specifier|public
name|Book
name|getBook
parameter_list|()
block|{
return|return
name|book
return|;
block|}
specifier|public
name|void
name|setBook
parameter_list|(
name|Book
name|book
parameter_list|)
block|{
name|this
operator|.
name|book
operator|=
name|book
expr_stmt|;
block|}
annotation|@
name|Id
specifier|public
name|int
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|ElementCollection
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getAuthors
parameter_list|()
block|{
return|return
name|authors
return|;
block|}
specifier|public
name|void
name|setAuthors
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|authors
parameter_list|)
block|{
name|this
operator|.
name|authors
operator|=
name|authors
expr_stmt|;
block|}
specifier|public
enum|enum
name|Review
block|{
name|GOOD
block|,
name|BAD
block|}
block|}
end_class

end_unit

