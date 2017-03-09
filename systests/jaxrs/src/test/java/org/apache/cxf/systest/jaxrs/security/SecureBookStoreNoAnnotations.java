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
name|security
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
name|ws
operator|.
name|rs
operator|.
name|Path
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
name|systest
operator|.
name|jaxrs
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
name|systest
operator|.
name|jaxrs
operator|.
name|BookInterface
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
name|systest
operator|.
name|jaxrs
operator|.
name|BookNotFoundFault
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/bookstorestorage/"
argument_list|)
specifier|public
class|class
name|SecureBookStoreNoAnnotations
implements|implements
name|BookInterface
block|{
specifier|private
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
name|SecureBookStoreNoAnnotations
parameter_list|()
block|{
name|Book
name|book
init|=
operator|new
name|Book
argument_list|()
decl_stmt|;
name|book
operator|.
name|setId
argument_list|(
literal|123L
argument_list|)
expr_stmt|;
name|book
operator|.
name|setName
argument_list|(
literal|"CXF in Action"
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
name|getThatBook
parameter_list|(
name|Long
name|id
parameter_list|)
block|{
return|return
name|books
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
specifier|public
name|Book
name|getThatBook
parameter_list|(
name|Long
name|id
parameter_list|,
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
name|s
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
return|return
name|books
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
specifier|public
name|Book
name|getThatBook
parameter_list|()
throws|throws
name|BookNotFoundFault
block|{
return|return
name|books
operator|.
name|get
argument_list|(
literal|123L
argument_list|)
return|;
block|}
specifier|public
name|Book
name|echoBook
parameter_list|(
name|Book
name|b
parameter_list|)
block|{
return|return
name|b
return|;
block|}
block|}
end_class

end_unit

