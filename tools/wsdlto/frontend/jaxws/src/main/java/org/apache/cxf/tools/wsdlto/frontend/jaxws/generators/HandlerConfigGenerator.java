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
name|generators
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|javax
operator|.
name|jws
operator|.
name|HandlerChain
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
name|staxutils
operator|.
name|StaxUtils
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
name|ToolConstants
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
name|model
operator|.
name|JAnnotation
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
name|JAnnotationElement
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
name|JavaInterface
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

begin_class
specifier|public
class|class
name|HandlerConfigGenerator
extends|extends
name|AbstractJAXWSGenerator
block|{
specifier|private
specifier|static
specifier|final
name|String
name|HANDLER_CHAIN_NAME
init|=
literal|""
decl_stmt|;
specifier|private
name|JavaInterface
name|intf
decl_stmt|;
specifier|private
name|JAnnotation
name|handlerChainAnnotation
decl_stmt|;
specifier|public
name|HandlerConfigGenerator
parameter_list|()
block|{
name|this
operator|.
name|name
operator|=
name|ToolConstants
operator|.
name|HANDLER_GENERATOR
expr_stmt|;
block|}
specifier|public
name|JAnnotation
name|getHandlerAnnotation
parameter_list|()
block|{
return|return
name|handlerChainAnnotation
return|;
block|}
specifier|public
name|boolean
name|passthrough
parameter_list|()
block|{
comment|//TODO: enable the handler chain
comment|/* if (this.intf.getHandlerChains() == null) {             return true;         }*/
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|setJavaInterface
parameter_list|(
name|JavaInterface
name|javaInterface
parameter_list|)
block|{
name|this
operator|.
name|intf
operator|=
name|javaInterface
expr_stmt|;
block|}
specifier|public
name|void
name|generate
parameter_list|(
name|ToolContext
name|penv
parameter_list|)
throws|throws
name|ToolException
block|{
name|this
operator|.
name|env
operator|=
name|penv
expr_stmt|;
if|if
condition|(
name|passthrough
argument_list|()
condition|)
block|{
return|return;
block|}
name|Element
name|e
init|=
name|this
operator|.
name|intf
operator|.
name|getHandlerChains
argument_list|()
decl_stmt|;
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
name|e
argument_list|,
name|ToolConstants
operator|.
name|HANDLER_CHAINS_URI
argument_list|,
name|ToolConstants
operator|.
name|HANDLER_CHAIN
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
name|String
name|fName
init|=
name|ProcessorUtil
operator|.
name|getHandlerConfigFileName
argument_list|(
name|this
operator|.
name|intf
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|handlerChainAnnotation
operator|=
operator|new
name|JAnnotation
argument_list|(
name|HandlerChain
operator|.
name|class
argument_list|)
expr_stmt|;
name|handlerChainAnnotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"name"
argument_list|,
name|HANDLER_CHAIN_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|handlerChainAnnotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"file"
argument_list|,
name|fName
operator|+
literal|".xml"
argument_list|)
argument_list|)
expr_stmt|;
name|generateHandlerChainFile
argument_list|(
name|e
argument_list|,
name|parseOutputName
argument_list|(
name|this
operator|.
name|intf
operator|.
name|getPackageName
argument_list|()
argument_list|,
name|fName
argument_list|,
literal|".xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|generateHandlerChainFile
parameter_list|(
name|Element
name|hChains
parameter_list|,
name|Writer
name|writer
parameter_list|)
throws|throws
name|ToolException
block|{
try|try
block|{
name|StaxUtils
operator|.
name|writeTo
argument_list|(
name|hChains
argument_list|,
name|writer
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

