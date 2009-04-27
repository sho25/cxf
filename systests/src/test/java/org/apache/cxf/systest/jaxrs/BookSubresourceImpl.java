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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|customer
operator|.
name|book
operator|.
name|BookNotFoundFault
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
specifier|public
name|Book
name|getTheBook3
parameter_list|(
name|String
name|sid
parameter_list|,
name|String
name|name
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
name|name
argument_list|)
expr_stmt|;
return|return
name|b
return|;
block|}
block|}
end_class

end_unit

