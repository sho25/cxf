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
name|staxutils
operator|.
name|transform
package|;
end_package

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
name|Map
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
name|NamespaceContext
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
name|XMLStreamConstants
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
name|XMLStreamException
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
name|staxutils
operator|.
name|DepthXMLStreamReader
import|;
end_import

begin_class
specifier|public
class|class
name|InTransformReader
extends|extends
name|DepthXMLStreamReader
block|{
specifier|private
specifier|static
specifier|final
name|String
name|INTERN_NAMES
init|=
literal|"org.codehaus.stax2.internNames"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|INTERN_NS
init|=
literal|"org.codehaus.stax2.internNsUris"
decl_stmt|;
specifier|private
name|QNamesMap
name|inElementsMap
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|QName
argument_list|,
name|QName
argument_list|>
name|inAppendMap
init|=
operator|new
name|HashMap
argument_list|<
name|QName
argument_list|,
name|QName
argument_list|>
argument_list|(
literal|5
argument_list|)
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
literal|5
argument_list|)
decl_stmt|;
specifier|private
name|QName
name|currentQName
decl_stmt|;
specifier|private
name|QName
name|previousQName
decl_stmt|;
specifier|private
name|int
name|previousDepth
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|boolean
name|blockOriginalReader
init|=
literal|true
decl_stmt|;
specifier|public
name|InTransformReader
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inMap
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|appendMap
parameter_list|,
name|boolean
name|blockOriginalReader
parameter_list|)
block|{
name|super
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|inElementsMap
operator|=
operator|new
name|QNamesMap
argument_list|(
name|inMap
operator|==
literal|null
condition|?
literal|0
else|:
name|inMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|blockOriginalReader
operator|=
name|blockOriginalReader
expr_stmt|;
name|TransformUtils
operator|.
name|convertToQNamesMap
argument_list|(
name|inMap
argument_list|,
name|inElementsMap
argument_list|,
name|nsMap
argument_list|)
expr_stmt|;
name|TransformUtils
operator|.
name|convertToMapOfQNames
argument_list|(
name|appendMap
argument_list|,
name|inAppendMap
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
comment|// If JAXB schema validation is disabled then returning
comment|// the native reader and thus bypassing this reader may work
specifier|public
name|XMLStreamReader
name|getReader
parameter_list|()
block|{
return|return
name|blockOriginalReader
condition|?
name|this
else|:
name|super
operator|.
name|getReader
argument_list|()
return|;
block|}
specifier|public
name|int
name|next
parameter_list|()
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|currentQName
operator|!=
literal|null
condition|)
block|{
return|return
name|XMLStreamConstants
operator|.
name|START_ELEMENT
return|;
block|}
elseif|else
if|if
condition|(
name|previousDepth
operator|!=
operator|-
literal|1
operator|&&
name|previousDepth
operator|==
name|getDepth
argument_list|()
operator|+
literal|1
condition|)
block|{
name|previousDepth
operator|=
operator|-
literal|1
expr_stmt|;
return|return
name|XMLStreamConstants
operator|.
name|END_ELEMENT
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|next
argument_list|()
return|;
block|}
block|}
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|INTERN_NAMES
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
name|INTERN_NS
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
return|return
name|super
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|String
name|getLocalName
parameter_list|()
block|{
name|QName
name|cQName
init|=
name|getCurrentName
argument_list|()
decl_stmt|;
if|if
condition|(
name|cQName
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|cQName
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|resetCurrentQName
argument_list|()
expr_stmt|;
return|return
name|name
return|;
block|}
return|return
name|super
operator|.
name|getLocalName
argument_list|()
return|;
block|}
specifier|private
name|QName
name|getCurrentName
parameter_list|()
block|{
return|return
name|currentQName
operator|!=
literal|null
condition|?
name|currentQName
else|:
name|previousQName
operator|!=
literal|null
condition|?
name|previousQName
else|:
literal|null
return|;
block|}
specifier|private
name|void
name|resetCurrentQName
parameter_list|()
block|{
name|currentQName
operator|=
name|previousQName
expr_stmt|;
name|previousQName
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|NamespaceContext
name|getNamespaceContext
parameter_list|()
block|{
return|return
operator|new
name|DelegatingNamespaceContext
argument_list|(
name|super
operator|.
name|getNamespaceContext
argument_list|()
argument_list|,
name|nsMap
argument_list|)
return|;
block|}
specifier|public
name|String
name|getNamespaceURI
parameter_list|()
block|{
name|QName
name|theName
init|=
name|readCurrentElement
argument_list|()
decl_stmt|;
name|QName
name|appendQName
init|=
name|inAppendMap
operator|.
name|remove
argument_list|(
name|theName
argument_list|)
decl_stmt|;
if|if
condition|(
name|appendQName
operator|!=
literal|null
condition|)
block|{
name|previousDepth
operator|=
name|getDepth
argument_list|()
expr_stmt|;
name|previousQName
operator|=
name|theName
expr_stmt|;
name|currentQName
operator|=
name|appendQName
expr_stmt|;
return|return
name|currentQName
operator|.
name|getNamespaceURI
argument_list|()
return|;
block|}
name|QName
name|expected
init|=
name|inElementsMap
operator|.
name|get
argument_list|(
name|theName
argument_list|)
decl_stmt|;
if|if
condition|(
name|expected
operator|==
literal|null
condition|)
block|{
return|return
name|theName
operator|.
name|getNamespaceURI
argument_list|()
return|;
block|}
name|currentQName
operator|=
name|expected
expr_stmt|;
return|return
name|currentQName
operator|.
name|getNamespaceURI
argument_list|()
return|;
block|}
specifier|private
name|QName
name|readCurrentElement
parameter_list|()
block|{
if|if
condition|(
name|currentQName
operator|!=
literal|null
condition|)
block|{
return|return
name|currentQName
return|;
block|}
name|String
name|ns
init|=
name|super
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|super
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
return|return
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|name
argument_list|)
return|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
operator|new
name|QName
argument_list|(
name|getNamespaceURI
argument_list|()
argument_list|,
name|getLocalName
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

