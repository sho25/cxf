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
name|aegis
operator|.
name|type
operator|.
name|java5
package|;
end_package

begin_class
specifier|public
class|class
name|XFireBean1
block|{
specifier|private
name|String
name|elementProperty
decl_stmt|;
specifier|private
name|String
name|attributeProperty
decl_stmt|;
specifier|private
name|String
name|bogusProperty
decl_stmt|;
annotation|@
name|XmlAttribute
specifier|public
name|String
name|getAttributeProperty
parameter_list|()
block|{
return|return
name|attributeProperty
return|;
block|}
specifier|public
name|void
name|setAttributeProperty
parameter_list|(
name|String
name|attributeProperty
parameter_list|)
block|{
name|this
operator|.
name|attributeProperty
operator|=
name|attributeProperty
expr_stmt|;
block|}
specifier|public
name|String
name|getBogusProperty
parameter_list|()
block|{
return|return
name|bogusProperty
return|;
block|}
specifier|public
name|void
name|setBogusProperty
parameter_list|(
name|String
name|bogusProperty
parameter_list|)
block|{
name|this
operator|.
name|bogusProperty
operator|=
name|bogusProperty
expr_stmt|;
block|}
annotation|@
name|XmlElement
argument_list|(
name|type
operator|=
name|CustomStringType
operator|.
name|class
argument_list|)
specifier|public
name|String
name|getElementProperty
parameter_list|()
block|{
return|return
name|elementProperty
return|;
block|}
specifier|public
name|void
name|setElementProperty
parameter_list|(
name|String
name|elementProperty
parameter_list|)
block|{
name|this
operator|.
name|elementProperty
operator|=
name|elementProperty
expr_stmt|;
block|}
block|}
end_class

end_unit

