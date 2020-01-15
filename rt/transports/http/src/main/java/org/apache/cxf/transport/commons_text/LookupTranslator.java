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
name|transport
operator|.
name|commons_text
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
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|InvalidParameterException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|BitSet
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
name|Map
import|;
end_import

begin_comment
comment|/**  * Translates a value using a lookup table.  *  * @since 1.0  */
end_comment

begin_class
class|class
name|LookupTranslator
extends|extends
name|CharSequenceTranslator
block|{
comment|/** The mapping to be used in translation. */
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|lookupMap
decl_stmt|;
comment|/** The first character of each key in the lookupMap. */
specifier|private
specifier|final
name|BitSet
name|prefixSet
decl_stmt|;
comment|/** The length of the shortest key in the lookupMap. */
specifier|private
specifier|final
name|int
name|shortest
decl_stmt|;
comment|/** The length of the longest key in the lookupMap. */
specifier|private
specifier|final
name|int
name|longest
decl_stmt|;
comment|/**      * Define the lookup table to be used in translation      *      * Note that, as of Lang 3.1 (the origin of this code), the key to the lookup      * table is converted to a java.lang.String. This is because we need the key      * to support hashCode and equals(Object), allowing it to be the key for a      * HashMap. See LANG-882.      *      * @param lookupMap Map&lt;CharSequence, CharSequence&gt; table of translator      *                  mappings      */
name|LookupTranslator
parameter_list|(
specifier|final
name|Map
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|lookupMap
parameter_list|)
block|{
if|if
condition|(
name|lookupMap
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvalidParameterException
argument_list|(
literal|"lookupMap cannot be null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|lookupMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|prefixSet
operator|=
operator|new
name|BitSet
argument_list|()
expr_stmt|;
name|int
name|currentShortest
init|=
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
name|int
name|currentLongest
init|=
literal|0
decl_stmt|;
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|CharSequence
argument_list|,
name|CharSequence
argument_list|>
name|pair
range|:
name|lookupMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|this
operator|.
name|lookupMap
operator|.
name|put
argument_list|(
name|pair
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|pair
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|prefixSet
operator|.
name|set
argument_list|(
name|pair
operator|.
name|getKey
argument_list|()
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|int
name|sz
init|=
name|pair
operator|.
name|getKey
argument_list|()
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|sz
operator|<
name|currentShortest
condition|)
block|{
name|currentShortest
operator|=
name|sz
expr_stmt|;
block|}
if|if
condition|(
name|sz
operator|>
name|currentLongest
condition|)
block|{
name|currentLongest
operator|=
name|sz
expr_stmt|;
block|}
block|}
name|this
operator|.
name|shortest
operator|=
name|currentShortest
expr_stmt|;
name|this
operator|.
name|longest
operator|=
name|currentLongest
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|int
name|translate
parameter_list|(
specifier|final
name|CharSequence
name|input
parameter_list|,
specifier|final
name|int
name|index
parameter_list|,
specifier|final
name|Writer
name|out
parameter_list|)
throws|throws
name|IOException
block|{
comment|// check if translation exists for the input at position index
if|if
condition|(
name|prefixSet
operator|.
name|get
argument_list|(
name|input
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
argument_list|)
condition|)
block|{
name|int
name|max
init|=
name|longest
decl_stmt|;
if|if
condition|(
name|index
operator|+
name|longest
operator|>
name|input
operator|.
name|length
argument_list|()
condition|)
block|{
name|max
operator|=
name|input
operator|.
name|length
argument_list|()
operator|-
name|index
expr_stmt|;
block|}
comment|// implement greedy algorithm by trying maximum match first
for|for
control|(
name|int
name|i
init|=
name|max
init|;
name|i
operator|>=
name|shortest
condition|;
name|i
operator|--
control|)
block|{
specifier|final
name|CharSequence
name|subSeq
init|=
name|input
operator|.
name|subSequence
argument_list|(
name|index
argument_list|,
name|index
operator|+
name|i
argument_list|)
decl_stmt|;
specifier|final
name|String
name|result
init|=
name|lookupMap
operator|.
name|get
argument_list|(
name|subSeq
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|i
return|;
block|}
block|}
block|}
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit
