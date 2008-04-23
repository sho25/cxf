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
name|wsdl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|ExtensibilityElement
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
name|XmlTransient
import|;
end_import

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

begin_comment
comment|/**  * Implements the<code>ExtensibilityElement</code> interface.  */
end_comment

begin_class
specifier|public
class|class
name|TExtensibilityElementImpl
extends|extends
name|TExtensibilityElement
implements|implements
name|ExtensibilityElement
block|{
annotation|@
name|XmlTransient
argument_list|()
name|QName
name|elementType
decl_stmt|;
comment|/**      * Returns the type of this extensibility element.      * @return QName the type of this element.      */
specifier|public
name|QName
name|getElementType
parameter_list|()
block|{
return|return
name|elementType
return|;
block|}
comment|/**      * Sets the type of this extensibility element.      * @param type QName the type of this element.      */
specifier|public
name|void
name|setElementType
parameter_list|(
name|QName
name|type
parameter_list|)
block|{
name|elementType
operator|=
name|type
expr_stmt|;
block|}
comment|/**      * Get whether or not the semantics of this extension are required.      * Relates to the wsdl:required attribute.      * @return Boolean      */
specifier|public
name|Boolean
name|getRequired
parameter_list|()
block|{
return|return
name|isSetRequired
argument_list|()
condition|?
name|isRequired
argument_list|()
else|:
literal|null
return|;
block|}
specifier|public
name|void
name|setRequired
parameter_list|(
name|Boolean
name|value
parameter_list|)
block|{
name|this
operator|.
name|required
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

end_unit

