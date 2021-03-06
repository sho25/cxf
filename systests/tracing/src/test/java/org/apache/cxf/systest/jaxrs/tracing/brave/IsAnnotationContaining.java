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
name|systest
operator|.
name|jaxrs
operator|.
name|tracing
operator|.
name|brave
package|;
end_package

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|Description
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|TypeSafeMatcher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|IsCollectionContaining
import|;
end_import

begin_import
import|import
name|zipkin2
operator|.
name|Annotation
import|;
end_import

begin_class
specifier|public
class|class
name|IsAnnotationContaining
extends|extends
name|IsCollectionContaining
argument_list|<
name|Annotation
argument_list|>
block|{
specifier|public
name|IsAnnotationContaining
parameter_list|(
specifier|final
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|TypeSafeMatcher
argument_list|<
name|Annotation
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|describeTo
parameter_list|(
name|Description
name|description
parameter_list|)
block|{
name|description
operator|.
name|appendText
argument_list|(
literal|"annotation with name "
argument_list|)
operator|.
name|appendValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|matchesSafely
parameter_list|(
name|Annotation
name|item
parameter_list|)
block|{
return|return
name|value
operator|.
name|equals
argument_list|(
name|item
operator|.
name|value
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|IsAnnotationContaining
name|hasItem
parameter_list|(
specifier|final
name|String
name|value
parameter_list|)
block|{
return|return
operator|new
name|IsAnnotationContaining
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

