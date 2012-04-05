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
name|transport
operator|.
name|http
operator|.
name|blueprint
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
name|aries
operator|.
name|blueprint
operator|.
name|mutable
operator|.
name|MutableBeanMetadata
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
name|AbstractBPBeanDefinitionParser
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
name|transport
operator|.
name|http
operator|.
name|AbstractHTTPDestination
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
name|HttpDestinationBPBeanDefinitionParser
extends|extends
name|AbstractBPBeanDefinitionParser
block|{
specifier|private
specifier|static
specifier|final
name|String
name|HTTP_NS
init|=
literal|"http://cxf.apache.org/transports/http/configuration"
decl_stmt|;
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
name|MutableBeanMetadata
name|bean
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutableBeanMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setRuntimeClass
argument_list|(
name|AbstractHTTPDestination
operator|.
name|class
argument_list|)
expr_stmt|;
name|mapElementToJaxbProperty
argument_list|(
name|context
argument_list|,
name|bean
argument_list|,
name|element
argument_list|,
operator|new
name|QName
argument_list|(
name|HTTP_NS
argument_list|,
literal|"server"
argument_list|)
argument_list|,
literal|"server"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|mapElementToJaxbProperty
argument_list|(
name|context
argument_list|,
name|bean
argument_list|,
name|element
argument_list|,
operator|new
name|QName
argument_list|(
name|HTTP_NS
argument_list|,
literal|"fixedParameterOrder"
argument_list|)
argument_list|,
literal|"fixedParameterOrder"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|mapElementToJaxbProperty
argument_list|(
name|context
argument_list|,
name|bean
argument_list|,
name|element
argument_list|,
operator|new
name|QName
argument_list|(
name|HTTP_NS
argument_list|,
literal|"contextMatchStrategy"
argument_list|)
argument_list|,
literal|"contextMatchStrategy"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|parseAttributes
argument_list|(
name|element
argument_list|,
name|context
argument_list|,
name|bean
argument_list|)
expr_stmt|;
name|parseChildElements
argument_list|(
name|element
argument_list|,
name|context
argument_list|,
name|bean
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setScope
argument_list|(
name|MutableBeanMetadata
operator|.
name|SCOPE_PROTOTYPE
argument_list|)
expr_stmt|;
return|return
name|bean
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|processNameAttribute
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|context
parameter_list|,
name|MutableBeanMetadata
name|bean
parameter_list|,
name|String
name|val
parameter_list|)
block|{
name|bean
operator|.
name|setId
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

