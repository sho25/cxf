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
name|common
operator|.
name|toolspec
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
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
name|w3c
operator|.
name|dom
operator|.
name|NodeList
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
name|i18n
operator|.
name|Message
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
name|logging
operator|.
name|LogUtils
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
name|helpers
operator|.
name|DOMUtils
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
name|helpers
operator|.
name|XPathUtils
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
name|ToolException
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
name|dom
operator|.
name|ExtendedDocumentBuilder
import|;
end_import

begin_class
specifier|public
class|class
name|ToolSpec
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|ToolSpec
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|ExtendedDocumentBuilder
name|builder
init|=
operator|new
name|ExtendedDocumentBuilder
argument_list|()
decl_stmt|;
specifier|private
name|Document
name|doc
decl_stmt|;
specifier|private
name|Tool
name|handler
decl_stmt|;
specifier|public
name|ToolSpec
parameter_list|()
block|{     }
specifier|public
name|ToolSpec
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|ToolException
block|{
name|this
argument_list|(
name|in
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ToolSpec
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|boolean
name|validate
parameter_list|)
throws|throws
name|ToolException
block|{
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Cannot create a ToolSpec object from a null stream"
argument_list|)
throw|;
block|}
try|try
block|{
name|builder
operator|.
name|setValidating
argument_list|(
name|validate
argument_list|)
expr_stmt|;
name|this
operator|.
name|doc
operator|=
name|builder
operator|.
name|parse
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Message
name|message
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_PARSING_TOOLSPCE_STREAM"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|message
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|ToolSpec
parameter_list|(
name|Document
name|d
parameter_list|)
block|{
if|if
condition|(
name|d
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Cannot create a ToolSpec object from "
operator|+
literal|"a null org.w3c.dom.Document"
argument_list|)
throw|;
block|}
name|this
operator|.
name|doc
operator|=
name|d
expr_stmt|;
block|}
specifier|public
name|ExtendedDocumentBuilder
name|getDocumentBuilder
parameter_list|()
block|{
return|return
name|builder
return|;
block|}
specifier|public
name|boolean
name|isValidInputStream
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|Element
name|streams
init|=
name|getStreams
argument_list|()
decl_stmt|;
if|if
condition|(
name|streams
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|List
argument_list|<
name|Element
argument_list|>
name|elemList
init|=
name|DOMUtils
operator|.
name|findAllElementsByTagNameNS
argument_list|(
name|streams
argument_list|,
name|Tool
operator|.
name|TOOL_SPEC_PUBLIC_ID
argument_list|,
literal|"instream"
argument_list|)
decl_stmt|;
for|for
control|(
name|Element
name|elem
range|:
name|elemList
control|)
block|{
if|if
condition|(
name|elem
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|Element
name|getElementById
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|Element
name|ele
init|=
name|doc
operator|.
name|getElementById
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|ele
operator|!=
literal|null
condition|)
block|{
return|return
name|ele
return|;
block|}
name|XPathUtils
name|xpather
init|=
operator|new
name|XPathUtils
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|NodeList
name|nl
init|=
operator|(
name|NodeList
operator|)
name|xpather
operator|.
name|getValue
argument_list|(
literal|"//*[@id='"
operator|+
name|id
operator|+
literal|"']"
argument_list|,
name|doc
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
decl_stmt|;
if|if
condition|(
name|nl
operator|!=
literal|null
operator|&&
name|nl
operator|.
name|getLength
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
operator|(
name|Element
operator|)
name|nl
operator|.
name|item
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|hasHandler
parameter_list|()
block|{
return|return
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|hasAttribute
argument_list|(
literal|"handler"
argument_list|)
return|;
block|}
specifier|public
name|Tool
name|getHandler
parameter_list|()
throws|throws
name|ToolException
block|{
if|if
condition|(
operator|!
name|hasHandler
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|handler
operator|==
literal|null
condition|)
block|{
name|String
name|handlerClz
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getAttribute
argument_list|(
literal|"handler"
argument_list|)
decl_stmt|;
try|try
block|{
name|handler
operator|=
operator|(
name|Tool
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|handlerClz
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Message
name|message
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_INSTANTIATE_HANDLER"
argument_list|,
name|LOG
argument_list|,
name|handlerClz
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|message
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
return|return
name|handler
return|;
block|}
specifier|public
name|Tool
name|getHandler
parameter_list|(
name|ClassLoader
name|loader
parameter_list|)
throws|throws
name|ToolException
block|{
if|if
condition|(
operator|!
name|hasHandler
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|handler
operator|==
literal|null
condition|)
block|{
name|String
name|handlerClz
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getAttribute
argument_list|(
literal|"handler"
argument_list|)
decl_stmt|;
try|try
block|{
name|handler
operator|=
operator|(
name|Tool
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|handlerClz
argument_list|,
literal|true
argument_list|,
name|loader
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Message
name|message
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_INSTANTIATE_HANDLER"
argument_list|,
name|LOG
argument_list|,
name|handlerClz
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|message
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
return|return
name|handler
return|;
block|}
specifier|public
name|Element
name|getStreams
parameter_list|()
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|elemList
init|=
name|DOMUtils
operator|.
name|findAllElementsByTagNameNS
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|,
name|Tool
operator|.
name|TOOL_SPEC_PUBLIC_ID
argument_list|,
literal|"streams"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|elemList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|elemList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getInstreamIds
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|res
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Element
name|streams
init|=
name|getStreams
argument_list|()
decl_stmt|;
if|if
condition|(
name|streams
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|elemList
init|=
name|DOMUtils
operator|.
name|findAllElementsByTagNameNS
argument_list|(
name|streams
argument_list|,
name|Tool
operator|.
name|TOOL_SPEC_PUBLIC_ID
argument_list|,
literal|"instream"
argument_list|)
decl_stmt|;
for|for
control|(
name|Element
name|elem
range|:
name|elemList
control|)
block|{
name|res
operator|.
name|add
argument_list|(
name|elem
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|res
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getOutstreamIds
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|res
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Element
name|streams
init|=
name|getStreams
argument_list|()
decl_stmt|;
if|if
condition|(
name|streams
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|elemList
init|=
name|DOMUtils
operator|.
name|findAllElementsByTagNameNS
argument_list|(
name|streams
argument_list|,
name|Tool
operator|.
name|TOOL_SPEC_PUBLIC_ID
argument_list|,
literal|"outstream"
argument_list|)
decl_stmt|;
for|for
control|(
name|Element
name|elem
range|:
name|elemList
control|)
block|{
name|res
operator|.
name|add
argument_list|(
name|elem
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|res
argument_list|)
return|;
block|}
specifier|public
name|Element
name|getUsage
parameter_list|()
block|{
return|return
name|DOMUtils
operator|.
name|findAllElementsByTagNameNS
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|,
name|Tool
operator|.
name|TOOL_SPEC_PUBLIC_ID
argument_list|,
literal|"usage"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
specifier|public
name|void
name|transform
parameter_list|(
name|InputStream
name|stylesheet
parameter_list|,
name|OutputStream
name|out
parameter_list|)
throws|throws
name|TransformerException
block|{
name|TransformerFactory
name|factory
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Transformer
name|trans
init|=
name|factory
operator|.
name|newTransformer
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|stylesheet
argument_list|)
argument_list|)
decl_stmt|;
name|trans
operator|.
name|transform
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
argument_list|,
operator|new
name|StreamResult
argument_list|(
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Element
name|getPipeline
parameter_list|()
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|elemList
init|=
name|DOMUtils
operator|.
name|findAllElementsByTagNameNS
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|,
name|Tool
operator|.
name|TOOL_SPEC_PUBLIC_ID
argument_list|,
literal|"pipeline"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|elemList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|elemList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|List
argument_list|<
name|Element
argument_list|>
name|getUsageForms
parameter_list|()
block|{
return|return
name|DOMUtils
operator|.
name|findAllElementsByTagNameNS
argument_list|(
name|getUsage
argument_list|()
argument_list|,
name|Tool
operator|.
name|TOOL_SPEC_PUBLIC_ID
argument_list|,
literal|"form"
argument_list|)
return|;
block|}
comment|/**      * Arguments can have streamref attributes which associate them with a      * stream. Tools usually request streams and rely on them being ready. If an      * argument is given a streamref, then the container constructs a stream      * from the argument value. This would usually be a simple FileInputStream      * or FileOutputStream. The mechanics of this are left for the container to      * sort out, but that is the reason why this getter method exists.      */
specifier|public
name|String
name|getStreamRefName
parameter_list|(
name|String
name|streamId
parameter_list|)
block|{
if|if
condition|(
name|getUsage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|elemList
init|=
name|DOMUtils
operator|.
name|findAllElementsByTagNameNS
argument_list|(
name|getUsage
argument_list|()
argument_list|,
name|Tool
operator|.
name|TOOL_SPEC_PUBLIC_ID
argument_list|,
literal|"associatedArgument"
argument_list|)
decl_stmt|;
for|for
control|(
name|Element
name|elem
range|:
name|elemList
control|)
block|{
if|if
condition|(
name|elem
operator|.
name|getAttribute
argument_list|(
literal|"streamref"
argument_list|)
operator|.
name|equals
argument_list|(
name|streamId
argument_list|)
condition|)
block|{
return|return
operator|(
operator|(
name|Element
operator|)
name|elem
operator|.
name|getParentNode
argument_list|()
operator|)
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
return|;
block|}
block|}
name|elemList
operator|=
name|DOMUtils
operator|.
name|findAllElementsByTagNameNS
argument_list|(
name|getUsage
argument_list|()
argument_list|,
name|Tool
operator|.
name|TOOL_SPEC_PUBLIC_ID
argument_list|,
literal|"argument"
argument_list|)
expr_stmt|;
for|for
control|(
name|Element
name|elem
range|:
name|elemList
control|)
block|{
if|if
condition|(
name|elem
operator|.
name|getAttribute
argument_list|(
literal|"streamref"
argument_list|)
operator|.
name|equals
argument_list|(
name|streamId
argument_list|)
condition|)
block|{
return|return
name|elem
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getParameterDefault
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Element
name|el
init|=
name|getElementById
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Element with id "
operator|+
name|name
operator|+
literal|" is "
operator|+
name|el
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"local name is "
operator|+
name|el
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|"argument"
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|el
operator|.
name|hasAttribute
argument_list|(
literal|"default"
argument_list|)
condition|)
block|{
return|return
name|el
operator|.
name|getAttribute
argument_list|(
literal|"default"
argument_list|)
return|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"option"
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|elemList
init|=
name|DOMUtils
operator|.
name|findAllElementsByTagNameNS
argument_list|(
name|el
argument_list|,
literal|"http://cxf.apache.org/Xpipe/ToolSpecification"
argument_list|,
literal|"associatedArgument"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|elemList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Element
name|assArg
init|=
name|elemList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|assArg
operator|.
name|hasAttribute
argument_list|(
literal|"default"
argument_list|)
condition|)
block|{
return|return
name|assArg
operator|.
name|getAttribute
argument_list|(
literal|"default"
argument_list|)
return|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getAnnotation
parameter_list|()
block|{
name|String
name|result
init|=
literal|null
decl_stmt|;
name|Element
name|element
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|Node
name|node
init|=
name|element
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|(
name|node
operator|.
name|getNodeType
argument_list|()
operator|==
name|Node
operator|.
name|ELEMENT_NODE
operator|)
operator|&&
operator|(
literal|"annotation"
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNodeName
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|result
operator|=
name|node
operator|.
name|getNodeValue
argument_list|()
expr_stmt|;
break|break;
block|}
name|node
operator|=
name|node
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

