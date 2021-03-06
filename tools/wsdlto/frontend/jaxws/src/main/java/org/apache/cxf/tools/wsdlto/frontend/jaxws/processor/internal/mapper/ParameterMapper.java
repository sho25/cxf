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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|internal
operator|.
name|mapper
package|;
end_package

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
name|adapters
operator|.
name|HexBinaryAdapter
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
name|common
operator|.
name|jaxb
operator|.
name|JAXBUtils
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
name|service
operator|.
name|model
operator|.
name|MessagePartInfo
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
name|ToolContext
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
name|JavaMethod
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
name|JavaParameter
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
name|JavaType
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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|internal
operator|.
name|ProcessorUtil
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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|internal
operator|.
name|annotator
operator|.
name|XmlJavaTypeAdapterAnnotator
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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|internal
operator|.
name|annotator
operator|.
name|XmlListAnotator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaSimpleType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaSimpleTypeList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|constants
operator|.
name|Constants
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ParameterMapper
block|{
specifier|private
name|ParameterMapper
parameter_list|()
block|{     }
specifier|public
specifier|static
name|JavaParameter
name|map
parameter_list|(
name|JavaMethod
name|jm
parameter_list|,
name|MessagePartInfo
name|part
parameter_list|,
name|JavaType
operator|.
name|Style
name|style
parameter_list|,
name|ToolContext
name|context
parameter_list|)
block|{
name|String
name|name
init|=
name|ProcessorUtil
operator|.
name|mangleNameToVariableName
argument_list|(
name|part
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|namespace
init|=
name|ProcessorUtil
operator|.
name|resolvePartNamespace
argument_list|(
name|part
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|ProcessorUtil
operator|.
name|resolvePartType
argument_list|(
name|part
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|JavaParameter
name|parameter
init|=
operator|new
name|JavaParameter
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
name|namespace
argument_list|)
decl_stmt|;
name|parameter
operator|.
name|setPartName
argument_list|(
name|part
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|part
operator|.
name|getXmlSchema
argument_list|()
operator|instanceof
name|XmlSchemaSimpleType
condition|)
block|{
name|processXmlSchemaSimpleType
argument_list|(
operator|(
name|XmlSchemaSimpleType
operator|)
name|part
operator|.
name|getXmlSchema
argument_list|()
argument_list|,
name|jm
argument_list|,
name|parameter
argument_list|,
name|part
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|part
operator|.
name|getXmlSchema
argument_list|()
operator|instanceof
name|XmlSchemaElement
condition|)
block|{
name|XmlSchemaElement
name|element
init|=
operator|(
name|XmlSchemaElement
operator|)
name|part
operator|.
name|getXmlSchema
argument_list|()
decl_stmt|;
if|if
condition|(
name|element
operator|.
name|getSchemaType
argument_list|()
operator|instanceof
name|XmlSchemaSimpleType
condition|)
block|{
name|processXmlSchemaSimpleType
argument_list|(
operator|(
name|XmlSchemaSimpleType
operator|)
name|element
operator|.
name|getSchemaType
argument_list|()
argument_list|,
name|jm
argument_list|,
name|parameter
argument_list|,
name|part
argument_list|)
expr_stmt|;
block|}
block|}
name|parameter
operator|.
name|setQName
argument_list|(
name|ProcessorUtil
operator|.
name|getElementName
argument_list|(
name|part
argument_list|)
argument_list|)
expr_stmt|;
name|parameter
operator|.
name|setDefaultValueWriter
argument_list|(
name|ProcessorUtil
operator|.
name|getDefaultValueWriter
argument_list|(
name|part
argument_list|,
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|fullJavaName
init|=
name|ProcessorUtil
operator|.
name|getFullClzName
argument_list|(
name|part
argument_list|,
name|context
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|parameter
operator|.
name|setClassName
argument_list|(
name|fullJavaName
argument_list|)
expr_stmt|;
if|if
condition|(
name|style
operator|==
name|JavaType
operator|.
name|Style
operator|.
name|INOUT
operator|||
name|style
operator|==
name|JavaType
operator|.
name|Style
operator|.
name|OUT
condition|)
block|{
name|parameter
operator|.
name|setHolder
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|parameter
operator|.
name|setHolderName
argument_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|holderClass
init|=
name|fullJavaName
decl_stmt|;
if|if
condition|(
name|JAXBUtils
operator|.
name|holderClass
argument_list|(
name|fullJavaName
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|holderClass
operator|=
name|JAXBUtils
operator|.
name|holderClass
argument_list|(
name|fullJavaName
argument_list|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|parameter
operator|.
name|setClassName
argument_list|(
name|holderClass
argument_list|)
expr_stmt|;
block|}
name|parameter
operator|.
name|setStyle
argument_list|(
name|style
argument_list|)
expr_stmt|;
return|return
name|parameter
return|;
block|}
specifier|private
specifier|static
name|void
name|processXmlSchemaSimpleType
parameter_list|(
name|XmlSchemaSimpleType
name|xmlSchema
parameter_list|,
name|JavaMethod
name|jm
parameter_list|,
name|JavaParameter
name|parameter
parameter_list|,
name|MessagePartInfo
name|part
parameter_list|)
block|{
if|if
condition|(
name|xmlSchema
operator|.
name|getContent
argument_list|()
operator|instanceof
name|XmlSchemaSimpleTypeList
operator|&&
operator|(
operator|!
name|part
operator|.
name|isElement
argument_list|()
operator|||
operator|!
name|jm
operator|.
name|isWrapperStyle
argument_list|()
operator|)
condition|)
block|{
name|parameter
operator|.
name|annotate
argument_list|(
operator|new
name|XmlListAnotator
argument_list|(
name|jm
operator|.
name|getInterface
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Constants
operator|.
name|XSD_HEXBIN
operator|.
name|equals
argument_list|(
name|xmlSchema
operator|.
name|getQName
argument_list|()
argument_list|)
operator|&&
operator|(
operator|!
name|part
operator|.
name|isElement
argument_list|()
operator|||
operator|!
name|jm
operator|.
name|isWrapperStyle
argument_list|()
operator|)
condition|)
block|{
name|parameter
operator|.
name|annotate
argument_list|(
operator|new
name|XmlJavaTypeAdapterAnnotator
argument_list|(
name|jm
operator|.
name|getInterface
argument_list|()
argument_list|,
name|HexBinaryAdapter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

