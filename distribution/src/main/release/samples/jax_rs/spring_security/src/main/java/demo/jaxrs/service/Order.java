begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jaxrs
operator|.
name|service
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
name|PathParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"Order"
argument_list|)
specifier|public
class|class
name|Order
block|{
specifier|private
name|long
name|id
decl_stmt|;
specifier|private
name|String
name|description
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Long
argument_list|,
name|Product
argument_list|>
name|products
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|Order
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
block|}
specifier|public
name|long
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
name|long
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
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|d
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|d
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"products/{productId}/"
argument_list|)
specifier|public
name|Product
name|getProduct
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"productId"
argument_list|)
name|int
name|productId
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"----invoking getProduct with id: "
operator|+
name|productId
argument_list|)
expr_stmt|;
return|return
name|products
operator|.
name|get
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
name|productId
argument_list|)
argument_list|)
return|;
block|}
specifier|final
name|void
name|init
parameter_list|()
block|{
name|Product
name|p
init|=
operator|new
name|Product
argument_list|()
decl_stmt|;
name|p
operator|.
name|setId
argument_list|(
literal|323
argument_list|)
expr_stmt|;
name|p
operator|.
name|setDescription
argument_list|(
literal|"product 323"
argument_list|)
expr_stmt|;
name|products
operator|.
name|put
argument_list|(
name|p
operator|.
name|getId
argument_list|()
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

