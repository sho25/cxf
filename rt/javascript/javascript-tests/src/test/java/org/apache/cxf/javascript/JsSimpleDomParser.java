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
name|javascript
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
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
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|ContextFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|ScriptableObject
import|;
end_import

begin_comment
comment|/**  * A Rhino wrapper to define DOMParser.  */
end_comment

begin_class
specifier|public
class|class
name|JsSimpleDomParser
extends|extends
name|ScriptableObject
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|664129507418929844L
decl_stmt|;
specifier|private
name|DocumentBuilder
name|documentBuilder
decl_stmt|;
specifier|public
name|JsSimpleDomParser
parameter_list|()
block|{
try|try
block|{
name|DocumentBuilderFactory
name|documentBuilderFactory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|documentBuilderFactory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|documentBuilderFactory
operator|.
name|setCoalescing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|documentBuilder
operator|=
name|documentBuilderFactory
operator|.
name|newDocumentBuilder
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|void
name|register
parameter_list|(
name|ScriptableObject
name|scope
parameter_list|)
block|{
try|try
block|{
name|ScriptableObject
operator|.
name|defineClass
argument_list|(
name|scope
argument_list|,
name|JsSimpleDomParser
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
literal|"DOMParser"
return|;
block|}
comment|// CHECKSTYLE:OFF
specifier|public
name|Object
name|jsFunction_parseFromString
parameter_list|(
name|String
name|xml
parameter_list|,
name|String
name|mimeType
parameter_list|)
block|{
name|StringReader
name|reader
init|=
operator|new
name|StringReader
argument_list|(
name|xml
argument_list|)
decl_stmt|;
name|InputSource
name|inputSource
init|=
operator|new
name|InputSource
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|Document
name|document
decl_stmt|;
try|try
block|{
name|document
operator|=
name|documentBuilder
operator|.
name|parse
argument_list|(
name|inputSource
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|Context
name|context
init|=
name|ContextFactory
operator|.
name|getGlobal
argument_list|()
operator|.
name|enterContext
argument_list|()
decl_stmt|;
try|try
block|{
name|JsSimpleDomNode
name|domNode
init|=
operator|(
name|JsSimpleDomNode
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|getParentScope
argument_list|()
argument_list|,
literal|"Node"
argument_list|)
decl_stmt|;
name|domNode
operator|.
name|initialize
argument_list|(
name|document
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|domNode
return|;
block|}
finally|finally
block|{
name|Context
operator|.
name|exit
argument_list|()
expr_stmt|;
block|}
block|}
comment|// CHECKSTYLE:ON
block|}
end_class

end_unit

