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

begin_class
specifier|public
class|class
name|BookSubresourceImpl
implements|implements
name|BookSubresource
block|{
specifier|private
name|Long
name|id
decl_stmt|;
specifier|public
name|BookSubresourceImpl
parameter_list|()
block|{
name|id
operator|=
literal|123L
expr_stmt|;
block|}
specifier|public
name|BookSubresourceImpl
parameter_list|(
name|Long
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
specifier|public
name|Book
name|getTheBook
parameter_list|()
throws|throws
name|BookNotFoundFault
block|{
if|if
condition|(
name|id
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Book
name|b
init|=
operator|new
name|Book
argument_list|()
decl_stmt|;
name|b
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|b
operator|.
name|setName
argument_list|(
literal|"CXF in Action"
argument_list|)
expr_stmt|;
return|return
name|b
return|;
block|}
specifier|public
name|Book
name|getTheBook2
parameter_list|(
name|String
name|n1
parameter_list|,
name|String
name|n2
parameter_list|,
name|String
name|n3
parameter_list|,
name|String
name|n33
parameter_list|,
name|String
name|n4
parameter_list|,
name|String
name|n5
parameter_list|,
name|String
name|n6
parameter_list|)
throws|throws
name|BookNotFoundFault
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
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|b
operator|.
name|setName
argument_list|(
name|n1
operator|+
name|n2
operator|+
name|n3
operator|+
name|n33
operator|+
name|n4
operator|+
name|n5
operator|+
name|n6
argument_list|)
expr_stmt|;
return|return
name|b
return|;
block|}
annotation|@
name|Override
specifier|public
name|Book
name|getTheBook3
parameter_list|(
name|String
name|sid
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|nameParts
parameter_list|)
throws|throws
name|BookNotFoundFault
block|{
if|if
condition|(
name|nameParts
operator|.
name|size
argument_list|()
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Wrong number of name parts"
argument_list|)
throw|;
block|}
name|Book
name|b
init|=
operator|new
name|Book
argument_list|()
decl_stmt|;
name|b
operator|.
name|setId
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
name|sid
argument_list|)
argument_list|)
expr_stmt|;
name|b
operator|.
name|setName
argument_list|(
name|nameParts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|+
name|nameParts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|b
return|;
block|}
specifier|public
name|Book
name|getTheBook4
parameter_list|(
name|Book
name|bookPath
parameter_list|,
name|Book
name|bookQuery
parameter_list|,
name|Book
name|bookMatrix
parameter_list|,
name|Book
name|formBook
parameter_list|)
throws|throws
name|BookNotFoundFault
block|{
if|if
condition|(
name|bookPath
operator|==
literal|null
operator|||
name|bookQuery
operator|==
literal|null
operator|||
name|bookMatrix
operator|==
literal|null
operator|||
name|formBook
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
name|long
name|id1
init|=
name|bookPath
operator|.
name|getId
argument_list|()
decl_stmt|;
name|long
name|id2
init|=
name|bookQuery
operator|.
name|getId
argument_list|()
decl_stmt|;
name|long
name|id3
init|=
name|bookMatrix
operator|.
name|getId
argument_list|()
decl_stmt|;
name|long
name|id4
init|=
name|formBook
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|id1
operator|!=
literal|139L
operator|||
name|id1
operator|!=
name|id2
operator|||
name|id1
operator|!=
name|id3
operator|||
name|id1
operator|!=
name|id4
operator|||
name|id1
operator|!=
name|id
operator|.
name|longValue
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
name|String
name|name1
init|=
name|bookPath
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|name2
init|=
name|bookQuery
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|name3
init|=
name|bookMatrix
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|name4
init|=
name|formBook
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
literal|"CXF Rocks"
operator|.
name|equals
argument_list|(
name|name1
argument_list|)
operator|||
operator|!
name|name1
operator|.
name|equals
argument_list|(
name|name2
argument_list|)
operator|||
operator|!
name|name1
operator|.
name|equals
argument_list|(
name|name3
argument_list|)
operator|||
operator|!
name|name1
operator|.
name|equals
argument_list|(
name|name4
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
return|return
name|bookPath
return|;
block|}
specifier|public
name|Book
name|getTheBookNoProduces
parameter_list|()
throws|throws
name|BookNotFoundFault
block|{
return|return
name|getTheBook
argument_list|()
return|;
block|}
specifier|public
name|OrderBean
name|addOrder
parameter_list|(
name|OrderBean
name|order
parameter_list|)
block|{
return|return
name|order
return|;
block|}
specifier|public
name|Book
name|getTheBookWithContext
parameter_list|(
name|UriInfo
name|ui
parameter_list|)
throws|throws
name|BookNotFoundFault
block|{
return|return
name|getTheBook
argument_list|()
return|;
block|}
specifier|public
name|Book
name|getTheBook5
parameter_list|(
name|String
name|name
parameter_list|,
name|long
name|bookid
parameter_list|)
throws|throws
name|BookNotFoundFault
block|{
return|return
operator|new
name|Book
argument_list|(
name|name
argument_list|,
name|bookid
argument_list|)
return|;
block|}
specifier|public
name|BookBean
name|getTheBookQueryBean
parameter_list|(
name|BookBean
name|book
parameter_list|)
throws|throws
name|BookNotFoundFault
block|{
name|Map
argument_list|<
name|Long
argument_list|,
name|String
argument_list|>
name|comments
init|=
name|book
operator|.
name|getComments
argument_list|()
decl_stmt|;
name|String
name|comment1
init|=
name|comments
operator|.
name|get
argument_list|(
literal|1L
argument_list|)
decl_stmt|;
name|String
name|comment2
init|=
name|comments
operator|.
name|get
argument_list|(
literal|2L
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"Good"
operator|.
name|equals
argument_list|(
name|comment1
argument_list|)
operator|&&
literal|"Good"
operator|.
name|equals
argument_list|(
name|comment2
argument_list|)
condition|)
block|{
return|return
name|book
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

