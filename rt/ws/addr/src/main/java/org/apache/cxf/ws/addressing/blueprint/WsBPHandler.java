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
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|NamespaceHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|ParserContext
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
name|configuration
operator|.
name|blueprint
operator|.
name|SimpleBPBeanDefinitionParser
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
name|WSAddressingFeature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|reflect
operator|.
name|ComponentMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|reflect
operator|.
name|Metadata
import|;
end_import

begin_class
specifier|public
class|class
name|WsBPHandler
implements|implements
name|NamespaceHandler
block|{
specifier|public
name|URL
name|getSchemaLocation
parameter_list|(
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
literal|"http://cxf.apache.org/ws/addressing"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"schemas/ws-addr-conf.xsd"
argument_list|)
return|;
block|}
comment|// no imported XSDs, so we don't have to delegate to cxf-core namespace handler
return|return
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|Set
argument_list|<
name|Class
argument_list|>
name|getManagedClasses
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Metadata
name|parse
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|SimpleBPBeanDefinitionParser
argument_list|(
name|WSAddressingFeature
operator|.
name|class
argument_list|)
operator|.
name|parse
argument_list|(
name|element
argument_list|,
name|context
argument_list|)
return|;
block|}
specifier|public
name|ComponentMetadata
name|decorate
parameter_list|(
name|Node
name|node
parameter_list|,
name|ComponentMetadata
name|componentMetadata
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

