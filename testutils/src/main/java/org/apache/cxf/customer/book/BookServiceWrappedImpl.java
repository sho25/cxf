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
name|customer
operator|.
name|book
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
name|jws
operator|.
name|WebService
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.apache.cxf.customer.book.BookService"
argument_list|)
specifier|public
class|class
name|BookServiceWrappedImpl
implements|implements
name|BookServiceWrapped
block|{
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
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|BookServiceWrappedImpl
parameter_list|()
block|{
name|Book
name|book
init|=
name|createBook
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Register the Book's id "
operator|+
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|books
operator|.
name|put
argument_list|(
name|book
operator|.
name|getId
argument_list|()
argument_list|,
name|book
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Book
name|getBook
parameter_list|(
name|long
name|bookid
parameter_list|)
throws|throws
name|BookNotFoundFault
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Long
argument_list|,
name|Book
argument_list|>
name|me
range|:
name|books
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"getBook -> "
operator|+
name|me
operator|.
name|getKey
argument_list|()
operator|+
literal|" : "
operator|+
name|me
operator|.
name|getValue
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|", "
operator|+
name|me
operator|.
name|getValue
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"The Book's id "
operator|+
name|bookid
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
name|books
operator|.
name|get
argument_list|(
name|bookid
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
name|BookNotFoundDetails
name|details
init|=
operator|new
name|BookNotFoundDetails
argument_list|()
decl_stmt|;
name|details
operator|.
name|setId
argument_list|(
name|bookid
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BookNotFoundFault
argument_list|(
name|details
argument_list|)
throw|;
block|}
return|return
name|b
return|;
block|}
specifier|final
name|Book
name|createBook
parameter_list|()
block|{
name|Book
name|b
init|=
operator|new
name|Book
argument_list|()
decl_stmt|;
name|b
operator|.
name|setName
argument_list|(
literal|"CXF in Action"
argument_list|)
expr_stmt|;
name|b
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
expr_stmt|;
return|return
name|b
return|;
block|}
block|}
end_class

end_unit

