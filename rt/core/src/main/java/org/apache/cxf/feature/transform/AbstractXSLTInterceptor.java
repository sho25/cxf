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
name|feature
operator|.
name|transform
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
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
name|Templates
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
name|TransformerConfigurationException
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
name|stream
operator|.
name|StreamSource
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|message
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
name|message
operator|.
name|MessageUtils
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
name|phase
operator|.
name|AbstractPhaseInterceptor
import|;
end_import

begin_comment
comment|/**  * Creates an XMLStreamReader from the InputStream on the Message.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractXSLTInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|TransformerFactory
name|TRANSFORM_FACTORIY
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
specifier|private
name|String
name|contextPropertyName
decl_stmt|;
specifier|private
specifier|final
name|Templates
name|xsltTemplate
decl_stmt|;
specifier|public
name|AbstractXSLTInterceptor
parameter_list|(
name|String
name|phase
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|before
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|after
parameter_list|,
name|String
name|xsltPath
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
if|if
condition|(
name|before
operator|!=
literal|null
condition|)
block|{
name|addBefore
argument_list|(
name|before
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|after
operator|!=
literal|null
condition|)
block|{
name|addAfter
argument_list|(
name|after
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|InputStream
name|xsltStream
init|=
name|ClassLoaderUtils
operator|.
name|getResourceAsStream
argument_list|(
name|xsltPath
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|xsltStream
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot load XSLT from path: "
operator|+
name|xsltPath
argument_list|)
throw|;
block|}
name|Source
name|xsltSource
init|=
operator|new
name|StreamSource
argument_list|(
name|xsltStream
argument_list|)
decl_stmt|;
name|xsltTemplate
operator|=
name|TRANSFORM_FACTORIY
operator|.
name|newTemplates
argument_list|(
name|xsltSource
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TransformerConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot create XSLT template from path: %s, error: "
argument_list|,
name|xsltPath
argument_list|,
name|e
operator|.
name|getException
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setContextPropertyName
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
name|contextPropertyName
operator|=
name|propertyName
expr_stmt|;
block|}
specifier|protected
name|boolean
name|checkContextProperty
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|contextPropertyName
operator|!=
literal|null
operator|&&
operator|!
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|contextPropertyName
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|protected
name|Templates
name|getXSLTTemplate
parameter_list|()
block|{
return|return
name|xsltTemplate
return|;
block|}
block|}
end_class

end_unit

