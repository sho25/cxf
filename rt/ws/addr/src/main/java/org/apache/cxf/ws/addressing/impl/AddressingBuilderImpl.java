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
name|ws
operator|.
name|addressing
operator|.
name|impl
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
name|ws
operator|.
name|addressing
operator|.
name|AddressingBuilder
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
name|ws
operator|.
name|addressing
operator|.
name|AddressingConstants
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
name|ws
operator|.
name|addressing
operator|.
name|AddressingProperties
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
name|ws
operator|.
name|addressing
operator|.
name|Names
import|;
end_import

begin_comment
comment|/**  * Factory for WS-Addressing elements.  *<p>  * Note that the JAXB generated types are used directly to represent   * WS-Addressing schema types. Hence there are no factory methods defined  * on this class for those types, as they may be instanted in the normal  * way via the JAXB generated ObjectFactory.  */
end_comment

begin_class
specifier|public
class|class
name|AddressingBuilderImpl
extends|extends
name|AddressingBuilder
block|{
specifier|public
name|AddressingBuilderImpl
parameter_list|()
block|{     }
comment|//--AddressingType implementation
comment|/**      * @return WS-Addressing namespace URI      */
specifier|public
name|String
name|getNamespaceURI
parameter_list|()
block|{
return|return
name|Names
operator|.
name|WSA_NAMESPACE_NAME
return|;
block|}
comment|//--AddresingBuilder implementation
comment|/**      * AddressingProperties factory method.      *        * @return a new AddressingProperties instance      */
specifier|public
name|AddressingProperties
name|newAddressingProperties
parameter_list|()
block|{
return|return
operator|new
name|AddressingPropertiesImpl
argument_list|()
return|;
block|}
comment|/**      * AddressingConstants factory method.      *       * @return an AddressingConstants instance      */
specifier|public
name|AddressingConstants
name|newAddressingConstants
parameter_list|()
block|{
return|return
operator|new
name|AddressingConstantsImpl
argument_list|()
return|;
block|}
block|}
end_class

end_unit

