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
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|tika
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|ParamConverterProvider
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
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|DefaultParamConverterProvider
import|;
end_import

begin_class
specifier|public
class|class
name|LuceneDocumentMetadata
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SOURCE_FIELD
init|=
literal|"source"
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|fieldTypes
decl_stmt|;
specifier|private
specifier|final
name|String
name|contentFieldName
decl_stmt|;
specifier|private
name|String
name|source
decl_stmt|;
specifier|private
name|ParamConverterProvider
name|converterProvider
init|=
operator|new
name|DefaultParamConverterProvider
argument_list|()
decl_stmt|;
specifier|public
name|LuceneDocumentMetadata
parameter_list|()
block|{
name|this
argument_list|(
literal|"contents"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|LuceneDocumentMetadata
parameter_list|(
specifier|final
name|String
name|contentFieldName
parameter_list|)
block|{
name|this
argument_list|(
name|contentFieldName
argument_list|,
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|LuceneDocumentMetadata
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|fieldTypes
parameter_list|)
block|{
name|this
argument_list|(
literal|"contents"
argument_list|,
name|fieldTypes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|LuceneDocumentMetadata
parameter_list|(
specifier|final
name|String
name|contentFieldName
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|fieldTypes
parameter_list|)
block|{
name|this
operator|.
name|contentFieldName
operator|=
name|contentFieldName
expr_stmt|;
name|this
operator|.
name|fieldTypes
operator|=
name|fieldTypes
expr_stmt|;
block|}
specifier|public
name|LuceneDocumentMetadata
name|withField
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|fieldTypes
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|LuceneDocumentMetadata
name|withFieldTypeConverter
parameter_list|(
specifier|final
name|ParamConverterProvider
name|provider
parameter_list|)
block|{
name|this
operator|.
name|converterProvider
operator|=
name|provider
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|LuceneDocumentMetadata
name|withSource
parameter_list|(
specifier|final
name|String
name|src
parameter_list|)
block|{
name|this
operator|.
name|source
operator|=
name|src
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getContentFieldName
parameter_list|()
block|{
return|return
name|contentFieldName
return|;
block|}
specifier|public
name|String
name|getSourceFieldName
parameter_list|()
block|{
return|return
name|SOURCE_FIELD
return|;
block|}
specifier|public
name|String
name|getSource
parameter_list|()
block|{
return|return
name|source
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getFieldType
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|fieldTypes
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getFieldTypes
parameter_list|()
block|{
return|return
name|fieldTypes
return|;
block|}
specifier|public
name|ParamConverterProvider
name|getFieldTypeConverter
parameter_list|()
block|{
return|return
name|converterProvider
return|;
block|}
block|}
end_class

end_unit

