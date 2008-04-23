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
name|java2wsdl
operator|.
name|generator
operator|.
name|wsdl11
operator|.
name|model
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JavaClass
import|;
end_import

begin_class
specifier|public
class|class
name|WrapperBeanClass
extends|extends
name|JavaClass
block|{
comment|/**      * Describe elementName here.      */
specifier|private
name|QName
name|elementName
decl_stmt|;
comment|/**      * Get the<code>ElementName</code> value.      *      * @return a<code>QName</code> value      */
specifier|public
specifier|final
name|QName
name|getElementName
parameter_list|()
block|{
return|return
name|elementName
return|;
block|}
comment|/**      * Set the<code>ElementName</code> value.      *      * @param newElementName The new ElementName value.      */
specifier|public
specifier|final
name|void
name|setElementName
parameter_list|(
specifier|final
name|QName
name|newElementName
parameter_list|)
block|{
name|this
operator|.
name|elementName
operator|=
name|newElementName
expr_stmt|;
block|}
block|}
end_class

end_unit

