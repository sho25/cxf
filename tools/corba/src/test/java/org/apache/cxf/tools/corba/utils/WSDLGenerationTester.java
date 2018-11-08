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
name|corba
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Definition
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|factory
operator|.
name|WSDLFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|xml
operator|.
name|WSDLReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|xml
operator|.
name|WSDLWriter
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

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|Bus
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
name|BusFactory
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
name|wsdl
operator|.
name|WSDLManager
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
name|wsdl11
operator|.
name|CatalogWSDLLocator
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
name|XmlSchema
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
name|XmlSchemaCollection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLGenerationTester
block|{
specifier|private
name|XmlSchemaCollection
name|schemaCol
init|=
operator|new
name|XmlSchemaCollection
argument_list|()
decl_stmt|;
specifier|public
name|WSDLGenerationTester
parameter_list|()
block|{     }
specifier|public
name|void
name|compare
parameter_list|(
name|XMLStreamReader
name|orig
parameter_list|,
name|XMLStreamReader
name|actual
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|origEnd
init|=
literal|false
decl_stmt|;
name|boolean
name|actualEnd
init|=
literal|false
decl_stmt|;
name|QName
name|elName
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|orig
operator|.
name|hasNext
argument_list|()
operator|||
name|actual
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|int
name|origTag
init|=
name|orig
operator|.
name|next
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|orig
operator|.
name|isStartElement
argument_list|()
operator|&&
operator|!
name|orig
operator|.
name|isEndElement
argument_list|()
operator|&&
operator|!
name|orig
operator|.
name|isCharacters
argument_list|()
condition|)
block|{
if|if
condition|(
name|orig
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|origTag
operator|=
name|orig
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|origEnd
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|int
name|actualTag
init|=
name|actual
operator|.
name|next
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|actual
operator|.
name|isStartElement
argument_list|()
operator|&&
operator|!
name|actual
operator|.
name|isEndElement
argument_list|()
operator|&&
operator|!
name|actual
operator|.
name|isCharacters
argument_list|()
condition|)
block|{
if|if
condition|(
name|actual
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|actualTag
operator|=
name|actual
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|actualEnd
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|origEnd
operator|&&
operator|!
name|actualEnd
condition|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"XML mismatch"
argument_list|,
name|origTag
argument_list|,
name|actualTag
argument_list|)
expr_stmt|;
if|if
condition|(
name|orig
operator|.
name|isStartElement
argument_list|()
condition|)
block|{
name|elName
operator|=
name|orig
operator|.
name|getName
argument_list|()
expr_stmt|;
name|compareStartElement
argument_list|(
name|orig
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|orig
operator|.
name|isEndElement
argument_list|()
condition|)
block|{
name|compareEndElement
argument_list|(
name|orig
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|orig
operator|.
name|isCharacters
argument_list|()
condition|)
block|{
name|compareCharacters
argument_list|(
name|elName
argument_list|,
name|orig
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
break|break;
block|}
block|}
block|}
specifier|private
name|void
name|compareStartElement
parameter_list|(
name|XMLStreamReader
name|orig
parameter_list|,
name|XMLStreamReader
name|actual
parameter_list|)
throws|throws
name|Exception
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Start element is not matched"
argument_list|,
name|orig
operator|.
name|getName
argument_list|()
argument_list|,
name|actual
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|origAttrCount
init|=
name|orig
operator|.
name|getAttributeCount
argument_list|()
decl_stmt|;
name|int
name|actualAttrCount
init|=
name|actual
operator|.
name|getAttributeCount
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|origAttrCount
condition|;
name|i
operator|++
control|)
block|{
name|QName
name|origAttrName
init|=
name|orig
operator|.
name|getAttributeName
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"location"
operator|.
name|equals
argument_list|(
name|origAttrName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|||
literal|"schemaLocation"
operator|.
name|equals
argument_list|(
name|origAttrName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
comment|//skip this atribute
name|origAttrCount
operator|--
expr_stmt|;
block|}
else|else
block|{
name|String
name|s1
init|=
name|orig
operator|.
name|getAttributeValue
argument_list|(
name|origAttrName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|origAttrName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|s2
init|=
name|actual
operator|.
name|getAttributeValue
argument_list|(
name|origAttrName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|origAttrName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|s1
operator|.
name|equals
argument_list|(
name|s2
argument_list|)
operator|&&
operator|(
name|s1
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
operator|||
name|s2
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
operator|)
condition|)
block|{
name|s1
operator|=
name|mapToQName
argument_list|(
name|orig
argument_list|,
name|s1
argument_list|)
expr_stmt|;
name|s2
operator|=
name|mapToQName
argument_list|(
name|actual
argument_list|,
name|s2
argument_list|)
expr_stmt|;
block|}
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Attribute "
operator|+
name|origAttrName
operator|+
literal|" not found or value not matching"
argument_list|,
name|s1
argument_list|,
name|s2
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|actualAttrCount
condition|;
name|i
operator|++
control|)
block|{
name|QName
name|actualAttrName
init|=
name|actual
operator|.
name|getAttributeName
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"location"
operator|.
name|equals
argument_list|(
name|actualAttrName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|||
literal|"schemaLocation"
operator|.
name|equals
argument_list|(
name|actualAttrName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
comment|//skip this atribute
name|actualAttrCount
operator|--
expr_stmt|;
block|}
block|}
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Attribute count is not matched for element "
operator|+
name|orig
operator|.
name|getName
argument_list|()
argument_list|,
name|origAttrCount
argument_list|,
name|actualAttrCount
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|mapToQName
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|,
name|String
name|s2
parameter_list|)
block|{
name|int
name|idx
init|=
name|s2
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|String
name|ns
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|idx
operator|==
operator|-
literal|1
condition|)
block|{
name|ns
operator|=
name|reader
operator|.
name|getNamespaceURI
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ns
operator|=
name|reader
operator|.
name|getNamespaceURI
argument_list|(
name|s2
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|ns
operator|==
literal|null
condition|)
block|{
name|ns
operator|=
name|reader
operator|.
name|getNamespaceURI
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|s2
operator|=
name|s2
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|s2
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|compareEndElement
parameter_list|(
name|XMLStreamReader
name|orig
parameter_list|,
name|XMLStreamReader
name|actual
parameter_list|)
throws|throws
name|Exception
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"End element is not matched"
argument_list|,
name|orig
operator|.
name|getName
argument_list|()
argument_list|,
name|actual
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|compareCharacters
parameter_list|(
name|QName
name|elName
parameter_list|,
name|XMLStreamReader
name|orig
parameter_list|,
name|XMLStreamReader
name|actual
parameter_list|)
throws|throws
name|Exception
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Element Characters not matched "
operator|+
name|elName
argument_list|,
name|orig
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|actual
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|File
name|writeDefinition
parameter_list|(
name|File
name|targetDir
parameter_list|,
name|File
name|defnFile
parameter_list|)
throws|throws
name|Exception
block|{
name|WSDLManager
name|wm
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|File
name|bkFile
init|=
operator|new
name|File
argument_list|(
name|targetDir
argument_list|,
literal|"bk_"
operator|+
name|defnFile
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|FileWriter
name|writer
init|=
operator|new
name|FileWriter
argument_list|(
name|bkFile
argument_list|)
decl_stmt|;
name|WSDLFactory
name|factory
init|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|(
literal|"org.apache.cxf.tools.corba.utils.TestWSDLCorbaFactoryImpl"
argument_list|)
decl_stmt|;
name|WSDLReader
name|reader
init|=
name|factory
operator|.
name|newWSDLReader
argument_list|()
decl_stmt|;
name|reader
operator|.
name|setFeature
argument_list|(
literal|"javax.wsdl.importDocuments"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|reader
operator|.
name|setExtensionRegistry
argument_list|(
name|wm
operator|.
name|getExtensionRegistry
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|url
init|=
name|defnFile
operator|.
name|toString
argument_list|()
decl_stmt|;
name|CatalogWSDLLocator
name|locator
init|=
operator|new
name|CatalogWSDLLocator
argument_list|(
name|url
argument_list|,
operator|(
name|Bus
operator|)
literal|null
argument_list|)
decl_stmt|;
name|Definition
name|wsdlDefn
init|=
name|reader
operator|.
name|readWSDL
argument_list|(
name|locator
argument_list|)
decl_stmt|;
name|WSDLWriter
name|wsdlWriter
init|=
name|factory
operator|.
name|newWSDLWriter
argument_list|()
decl_stmt|;
name|wsdlWriter
operator|.
name|writeWSDL
argument_list|(
name|wsdlDefn
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|writer
operator|=
literal|null
expr_stmt|;
name|reader
operator|=
literal|null
expr_stmt|;
return|return
name|bkFile
return|;
block|}
specifier|public
name|File
name|writeSchema
parameter_list|(
name|File
name|targetDir
parameter_list|,
name|File
name|schemaFile
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|bkFile
init|=
operator|new
name|File
argument_list|(
name|targetDir
argument_list|,
literal|"bk_"
operator|+
name|schemaFile
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|FileWriter
name|writer
init|=
operator|new
name|FileWriter
argument_list|(
name|bkFile
argument_list|)
decl_stmt|;
name|FileReader
name|reader
init|=
operator|new
name|FileReader
argument_list|(
name|schemaFile
argument_list|)
decl_stmt|;
name|XmlSchema
name|schema
init|=
name|schemaCol
operator|.
name|read
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|schema
operator|.
name|write
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|writer
operator|=
literal|null
expr_stmt|;
name|reader
operator|=
literal|null
expr_stmt|;
return|return
name|bkFile
return|;
block|}
block|}
end_class

end_unit

