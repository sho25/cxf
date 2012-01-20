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
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|List
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
name|util
operator|.
name|URIParserUtil
import|;
end_import

begin_class
specifier|public
class|class
name|JavaField
extends|extends
name|JavaType
implements|implements
name|JavaAnnotatable
block|{
specifier|private
name|String
name|modifier
decl_stmt|;
specifier|private
name|List
argument_list|<
name|JAnnotation
argument_list|>
name|annotations
init|=
operator|new
name|ArrayList
argument_list|<
name|JAnnotation
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Annotation
index|[]
name|jaxbAnnotations
decl_stmt|;
specifier|public
name|JavaField
parameter_list|()
block|{     }
specifier|public
name|JavaField
parameter_list|(
name|String
name|n
parameter_list|,
name|String
name|t
parameter_list|,
name|String
name|tns
parameter_list|)
block|{
name|super
argument_list|(
name|n
argument_list|,
name|t
argument_list|,
name|tns
argument_list|)
expr_stmt|;
name|this
operator|.
name|modifier
operator|=
literal|"private"
expr_stmt|;
block|}
specifier|public
name|String
name|getModifier
parameter_list|()
block|{
return|return
name|this
operator|.
name|modifier
return|;
block|}
specifier|public
name|void
name|setModifier
parameter_list|(
name|String
name|modi
parameter_list|)
block|{
name|this
operator|.
name|modifier
operator|=
name|modi
expr_stmt|;
block|}
specifier|public
name|void
name|addAnnotation
parameter_list|(
name|JAnnotation
name|anno
parameter_list|)
block|{
name|annotations
operator|.
name|add
argument_list|(
name|anno
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|importClz
range|:
name|anno
operator|.
name|getImports
argument_list|()
control|)
block|{
name|getOwner
argument_list|()
operator|.
name|addImport
argument_list|(
name|importClz
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|JAnnotation
argument_list|>
name|getAnnotations
parameter_list|()
block|{
return|return
name|this
operator|.
name|annotations
return|;
block|}
specifier|public
name|void
name|annotate
parameter_list|(
name|Annotator
name|annotator
parameter_list|)
block|{
name|annotator
operator|.
name|annotate
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
comment|/*if (URIParserUtil.containsReservedKeywords(this.name)) {             return "_" + this.name;         }*/
return|return
name|this
operator|.
name|name
return|;
block|}
specifier|public
name|void
name|setJaxbAnnotations
parameter_list|(
name|Annotation
index|[]
name|anns
parameter_list|)
block|{
name|jaxbAnnotations
operator|=
name|anns
expr_stmt|;
block|}
specifier|public
name|Annotation
index|[]
name|getJaxbAnnotaions
parameter_list|()
block|{
if|if
condition|(
name|jaxbAnnotations
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|Annotation
index|[]
block|{}
return|;
block|}
return|return
name|jaxbAnnotations
return|;
block|}
specifier|public
name|String
name|getParaName
parameter_list|()
block|{
if|if
condition|(
name|URIParserUtil
operator|.
name|containsReservedKeywords
argument_list|(
name|this
operator|.
name|name
argument_list|)
condition|)
block|{
return|return
literal|"_"
operator|+
name|this
operator|.
name|name
return|;
block|}
return|return
name|this
operator|.
name|name
return|;
block|}
block|}
end_class

end_unit

