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
name|tools
operator|.
name|fortest
operator|.
name|exception
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
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
name|XmlAttribute
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
name|XmlElement
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
literal|"item"
argument_list|)
specifier|public
class|class
name|OrderItem
block|{
name|Long
name|sku
decl_stmt|;
name|Integer
name|count
decl_stmt|;
name|Double
name|pricePerUnit
decl_stmt|;
specifier|public
name|OrderItem
parameter_list|()
block|{     }
specifier|public
name|OrderItem
parameter_list|(
name|Long
name|sku
parameter_list|,
name|Integer
name|count
parameter_list|,
name|Double
name|pricePerUnit
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|sku
operator|=
name|sku
expr_stmt|;
name|this
operator|.
name|count
operator|=
name|count
expr_stmt|;
name|this
operator|.
name|pricePerUnit
operator|=
name|pricePerUnit
expr_stmt|;
block|}
annotation|@
name|XmlAttribute
specifier|public
name|Long
name|getSku
parameter_list|()
block|{
return|return
name|sku
return|;
block|}
specifier|public
name|void
name|setSku
parameter_list|(
name|Long
name|sku
parameter_list|)
block|{
name|this
operator|.
name|sku
operator|=
name|sku
expr_stmt|;
block|}
annotation|@
name|XmlElement
specifier|public
name|Integer
name|getCount
parameter_list|()
block|{
return|return
name|count
return|;
block|}
specifier|public
name|void
name|setCount
parameter_list|(
name|Integer
name|count
parameter_list|)
block|{
name|this
operator|.
name|count
operator|=
name|count
expr_stmt|;
block|}
annotation|@
name|XmlElement
specifier|public
name|Double
name|getPricePerUnit
parameter_list|()
block|{
return|return
name|pricePerUnit
return|;
block|}
specifier|public
name|void
name|setPricePerUnit
parameter_list|(
name|Double
name|pricePerUnit
parameter_list|)
block|{
name|this
operator|.
name|pricePerUnit
operator|=
name|pricePerUnit
expr_stmt|;
block|}
specifier|public
name|BigDecimal
name|getTotalPrice
parameter_list|()
block|{
return|return
operator|new
name|BigDecimal
argument_list|(
name|pricePerUnit
argument_list|)
operator|.
name|multiply
argument_list|(
operator|new
name|BigDecimal
argument_list|(
name|count
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

